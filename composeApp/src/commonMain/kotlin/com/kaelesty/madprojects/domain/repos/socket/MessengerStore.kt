package com.kaelesty.madprojects_kmp.blocs.project.messenger

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.repos.socket.Action
import com.kaelesty.madprojects.domain.repos.socket.Chat
import com.kaelesty.madprojects.domain.repos.socket.ChatSender
import com.kaelesty.madprojects.domain.repos.socket.Intent
import com.kaelesty.madprojects.domain.repos.socket.Message
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore.Label
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore.State
import kotlinx.coroutines.launch

interface MessengerStore : Store<MessengerStore.Intent, State, Label> {

	sealed interface Intent {

		data class OnChatSelected(val chatId: Int) : Intent

		data class SendMessage(val text: String, val chatId: Int): Intent

		data class ReadMessagesBefore(val messageId: Int, val chatId: Int): Intent

	}

	data class State(
		val chats: List<ChatState>,
		val senders: List<ChatSender>,
		val userId: Int,
	) {
		data class ChatState(
			val chat: Chat,
			val readMessages: List<Message>,
			val unreadMessages: List<Message>,
		)
	}

	sealed interface Label {
		data class NavigateToChat(val chatId: Int): Label
	}
}

private const val MAX_MESSAGE_SIZE = 256

class MessengerStoreFactory(
	private val storeFactory: StoreFactory,
	private val socket: SocketRepository,
) {

	fun create(
		userId: Int,
		projectId: Int,
	): MessengerStore =
		object : MessengerStore, Store<MessengerStore.Intent, State, Label> by storeFactory.create(
			name = "MessengerStore",
			initialState = State(listOf(), listOf(), -1),
			bootstrapper = BootstrapperImpl(socket),
			executorFactory = { ExecutorImpl(socket, projectId) },
			reducer = ReducerImpl(userId_ = userId)
		) {}

	private sealed interface Msg {
		data class AddChat(val chat: Chat) : Msg
		data class AddMessage(val message: Message, val chatId: Int) : Msg
		data class SetChatMessages(
			val readMessages: List<Message>,
			val unreadMessages: List<Message>,
			val chatId: Int,
			val userId: Int,
		) : Msg
		data class SetChatsList(val chats: List<Chat>, val senders: List<ChatSender>) : Msg
		data class ReadMessagesBefore(val messageId: Int, val chatId: Int) : Msg
		data class UpdateUnreadCount(val chatId: Int, val count: Int): Msg
	}

	private class BootstrapperImpl(
		private val socket: SocketRepository,
	) : CoroutineBootstrapper<Action>() {
		override fun invoke() {
			scope.launch {
				socket.action.collect {
					dispatch(it)
				}
			}
		}
	}

	private class ExecutorImpl(
		private val socket: SocketRepository,
		private val projectId: Int,
	) :
		CoroutineExecutor<MessengerStore.Intent, Action, State, Msg, Label>() {
		override fun executeIntent(intent: MessengerStore.Intent) {
			when (intent) {

				is MessengerStore.Intent.OnChatSelected -> {
					scope.launch {
						socket.accept(
							Intent.Messenger.RequestChatMessages(
								chatId = intent.chatId,
								projectId = projectId,
							)
						)
					}
					publish(Label.NavigateToChat(intent.chatId))
				}
				is MessengerStore.Intent.SendMessage -> {
					if (intent.text.isBlank() || intent.text.length > 256) return
					scope.launch {
						socket.accept(
							Intent.Messenger.SendMessage(
								message = intent.text.trim(),
								chatId = intent.chatId,
								projectId = projectId
							)
						)
					}
				}

				is MessengerStore.Intent.ReadMessagesBefore -> {
					scope.launch {
						socket.accept(
							Intent.Messenger.ReadMessagesBefore(
								messageId = intent.messageId,
								chatId = intent.chatId,
								projectId = projectId,
							)
						)

					}
					dispatch(Msg.ReadMessagesBefore(intent.messageId, intent.chatId))
				}
			}
		}

		override fun executeAction(action: Action) {
			if (action !is Action.Messenger) return
			when (action) {
				is Action.Messenger.NewChat -> dispatch(Msg.AddChat(chat = action.chat))
				is Action.Messenger.NewMessage -> dispatch(
					Msg.AddMessage(message = action.message, chatId = action.chatId)
				)

				is Action.Messenger.SendChatMessages -> dispatch(
					Msg.SetChatMessages(
						readMessages = action.readMessages,
						unreadMessages = action.unreadMessages,
						chatId = action.chatId,
						userId = action.userId.toInt()
					)
				)

				is Action.Messenger.SendChatsList -> dispatch(
					Msg.SetChatsList(chats = action.chats, senders = action.senders)
				)

				is Action.Messenger.MessageReadRecorded -> { /*TEMPORARY UNUSED*/ }
				is Action.Messenger.UpdateChatUnreadCount -> {
					dispatch(Msg.UpdateUnreadCount(action.chatId, action.count))
				}
			}
		}
	}

	private class ReducerImpl(private val userId_: Int) : Reducer<State, Msg> {
		override fun State.reduce(message: Msg): State =
			when (message) {

				is Msg.AddChat -> copy(
					chats = chats
						.toMutableList()
						.apply {
							add(
								State.ChatState(
									chat = message.chat,
									readMessages = listOf(),
									unreadMessages = listOf(),
								)
							)
						}
						.toList()
				)

				is Msg.AddMessage -> copy(
					chats = chats
						.map {
							if (it.chat.id == message.chatId) {
								if (message.message.senderId == userId.toString()) {
									it.copy(
										readMessages = it.readMessages
											.toMutableList()
											.apply {
												add(message.message)
											}
											.toList(),
										chat = it.chat.copy(
											lastMessage = message.message
										)
									)
								}
								else {
									it.copy(
										unreadMessages = it.unreadMessages
											.toMutableList()
											.apply {
												add(message.message)
											}
											.toList(),
										chat = it.chat.copy(
											lastMessage = message.message
										)
									)
								}
							}
							else it
						}
				)
				is Msg.SetChatMessages -> copy(
					chats = chats
						.map {
							if (it.chat.id == message.chatId) {
								it.copy(
									readMessages = message.readMessages,
									unreadMessages = message.unreadMessages
								)
							}
							else it
						},
					userId = userId_,
				)
				is Msg.SetChatsList -> copy(
					chats = message.chats.map {
						State.ChatState(chat = it, readMessages = listOf(), unreadMessages = listOf())
					},
					senders = message.senders
				)

				is Msg.ReadMessagesBefore -> copy(
					chats = chats.map {
						if (it.chat.id == message.chatId) {
							val messageIdsToRead = it.unreadMessages
								.filter { it.id <= message.messageId }
								.map { it.id }
							it.copy(
								readMessages = it.readMessages + it.unreadMessages
									.filter { it.id in messageIdsToRead },
								unreadMessages = it.unreadMessages
									.filter { it.id !in messageIdsToRead }
							)
						}
						else it
					}
				)

				is Msg.UpdateUnreadCount -> copy(
					chats = chats.map {
						if (it.chat.id == message.chatId) {
							it.copy(
								chat = it.chat.copy(
									unreadMessagesCount = message.count
								)
							)
						}
						else it
					}
				)
			}
	}
}
