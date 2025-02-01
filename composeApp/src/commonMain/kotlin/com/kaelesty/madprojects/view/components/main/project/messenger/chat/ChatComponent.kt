package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.kaelesty.madprojects.domain.repos.socket.Action
import com.kaelesty.madprojects.domain.repos.socket.Chat
import com.kaelesty.madprojects.domain.repos.socket.Message
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import com.kaelesty.madprojects.view.extensions.copyWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ChatComponent {

	val state: StateFlow<State>

	data class State(
		val chatId: Int = -1,
		val chatTitle: String = "None",
		val senders: List<MessageSender> = listOf(),
		val readBlocks: List<MessageBlock> = listOf(),
		val unreadBlocks: List<MessageBlock> = listOf(),
	) {
		data class MessageBlock(
			val senderId: Int,
			val messages: List<Message>,
			val type: MessageBlockType,
		) {
			enum class MessageBlockType {
				Incoming, Outcoming,
			}
		}

		data class MessageSender(
			val name: String,
			val id: Int,
			val avatarUrl: String,
		)
	}

	fun sendMessage(text: String)

	fun readMessage(messageId: Int)
}

class DefaultChatComponent(
	private val componentContext: ComponentContext,
	private val chatId: Int,
	private val socketRepository: SocketRepository,
): ComponentContext by componentContext, ChatComponent {

	data class CommonState(
		val chats: List<ChatState>
	) {
		data class ChatState(
			val chat: Chat,
			val readMessages: List<Message>,
			val unreadMessages: List<Message>,
		)
	}

	private val scope = CoroutineScope(Dispatchers.IO)

	private val messagesToRead: MutableStateFlow<MutableList<Int>?> = MutableStateFlow(null)

	init {
		lifecycle.subscribe(
			object : Lifecycle.Callbacks {
				override fun onCreate() {
					super.onCreate()
					scope.launch {
						socketRepository.action.collect {
							if (it is Action.Messenger) {
								proceedAction(it)
						}
						}
					}
				}
				override fun onDestroy() {
					super.onDestroy()
					scope.launch {
						sendReaded(
							onFinish = { scope.cancel() }
						)
					}
				}
			}
		)
	}

	private val commonState = MutableStateFlow<CommonState>(CommonState(listOf()))

	private val _state: MutableStateFlow<ChatComponent.State> = MutableStateFlow(ChatComponent.State())
	override val state: StateFlow<ChatComponent.State>
		get() = _state

	override fun sendMessage(text: String) {

	}

	override fun readMessage(messageId: Int) {
		if (messagesToRead.value == null) {
			messagesToRead.update { mutableListOf(messageId) }
			runReader()
		}
		else {
			messagesToRead.update { it?.apply { add(messageId) } }
		}
	}


	private suspend fun proceedAction(action: Action.Messenger) {
		when (action) {
			is Action.Messenger.MessageReadRecorded -> {  }
			is Action.Messenger.NewChat -> {
				with(commonState.value) {
					commonState.emit(
						copy(
							chats = chats.copyWith(
								CommonState.ChatState(
									chat = action.chat,
									readMessages = listOf(),
									unreadMessages = listOf(),
								)
							)
						)
					)
				}
			}
			is Action.Messenger.NewMessage -> {
				with(commonState.value) {
					commonState.emit(
						copy(
							chats = chats.map {
								if (it.chat.id == action.chatId) {
									it.copy(
										unreadMessages = it.unreadMessages.copyWith(
											action.message
										)
									)
								}
								else it
							}
						)
					)
				}
			}
			is Action.Messenger.SendChatMessages -> {
				with(commonState.value) {
					commonState.emit(
						copy(
							chats = chats.map {
								if (it.chat.id == action.chatId) {
									it.copy(
										unreadMessages = action.unreadMessages,
										readMessages = action.readMessages,
									)
								}
								else it
							}
						)
					)
				}
			}
			is Action.Messenger.SendChatsList -> TODO()
			is Action.Messenger.UpdateChatUnreadCount -> TODO()
		}
	}

	private fun runReader() {
		scope.launch(Dispatchers.IO) {
			delay(10*1000)
			sendReaded({})
		}
	}

	private suspend fun sendReaded(onFinish: () -> Unit) {
		val ids = messagesToRead.value
		messagesToRead.update { null }
		ids?.max()?.let {
			withContext(Dispatchers.Main) {
				store.accept(
					MessengerStore.Intent.ReadMessagesBefore(it, chatId)
				)
				onFinish()
			}
		}
	}
}