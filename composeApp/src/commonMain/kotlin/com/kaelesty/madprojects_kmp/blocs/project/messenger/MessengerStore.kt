package com.kaelesty.madprojects_kmp.blocs.project.messenger

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.messenger.Messenger
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore.Intent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore.Label
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore.State
import entities.Chat
import entities.ClientAction
import entities.Message
import entities.ServerAction
import kotlinx.coroutines.launch

interface MessengerStore : Store<Intent, State, Label> {

	sealed interface Intent {

		data class OnChatSelected(val chatId: Int) : Intent

		data class SendMessage(val text: String, val chatId: Int): Intent

		data class ReadMessagesBefore(val messageId: Int, val chatId: Int): Intent
	}

	data class State(
		val chats: List<ChatState>
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
	private val messenger: Messenger,
	private val userId: Int
) {


	fun create(): MessengerStore =
		object : MessengerStore, Store<Intent, State, Label> by storeFactory.create(
			name = "MessengerStore",
			initialState = State(listOf()),
			bootstrapper = BootstrapperImpl(messenger, userId),
			executorFactory = { ExecutorImpl(messenger) },
			reducer = ReducerImpl(userId = userId)
		) {}

	private sealed interface Msg {
		data class AddChat(val chat: Chat) : Msg
		data class AddMessage(val message: Message, val chatId: Int) : Msg
		data class SetChatMessages(
			val readMessages: List<Message>,
			val unreadMessages: List<Message>,
			val chatId: Int,
		) : Msg
		data class SetChatsList(val chats: List<Chat>) : Msg
		data class ReadMessagesBefore(val messageId: Int, val chatId: Int) : Msg
	}

	private class BootstrapperImpl(
		private val messenger: Messenger,
		private val userId: Int,
	) : CoroutineBootstrapper<ServerAction>() {
		override fun invoke() {
			scope.launch {
				messenger.connect {
					scope.launch {
						messenger.acceptAction(
							ClientAction.Authorize(
								jwt = userId.toString(), // TODO
								projectId = 1
							)
						)
						messenger.acceptAction(
							ClientAction.RequestChatsList(
								projectId = 1
							)
						)
					}
				}
			}
			scope.launch {
				messenger.actionsFlow.collect {
					dispatch(it)
				}
			}
		}
	}

	private class ExecutorImpl(private val messenger: Messenger) :
		CoroutineExecutor<Intent, ServerAction, State, Msg, Label>() {
		override fun executeIntent(intent: Intent) {
			when (intent) {

				is Intent.OnChatSelected -> {
					scope.launch {
						messenger.acceptAction(
							ClientAction.RequestChatMessages(chatId = intent.chatId)
						)
					}
					publish(Label.NavigateToChat(intent.chatId))
				}
				is Intent.SendMessage -> {
					if (intent.text.isBlank() || intent.text.length > 256) return
					scope.launch {
						messenger.acceptAction(
							ClientAction.SendMessage(
								message = intent.text.trim(),
								chatId = intent.chatId)
						)
					}
				}

				is Intent.ReadMessagesBefore -> {
					scope.launch {
						messenger.acceptAction(
							ClientAction.ReadMessagesBefore(
								messageId = intent.messageId,
								chatId = intent.chatId
							)
						)

					}
					dispatch(Msg.ReadMessagesBefore(intent.messageId, intent.chatId))
				}
			}
		}

		override fun executeAction(action: ServerAction) {
			when (action) {
				is ServerAction.NewChat -> dispatch(Msg.AddChat(chat = action.chat))
				is ServerAction.NewMessage -> dispatch(
					Msg.AddMessage(message = action.message, chatId = action.chatId)
				)

				is ServerAction.SendChatMessages -> dispatch(
					Msg.SetChatMessages(
						readMessages = action.readMessages,
						unreadMessages = action.unreadMessages,
						chatId = action.chatId,
					)
				)

				is ServerAction.SendChatsList -> dispatch(
					Msg.SetChatsList(chats = action.chats)
				)

				is ServerAction.MessageReadRecorded -> { /*TEMPORARY UNUSED*/ }
			}
		}
	}

	private class ReducerImpl(private val userId: Int) : Reducer<State, Msg> {
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
								if (message.message.senderId == userId) {
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
						}
				)
				is Msg.SetChatsList -> copy(
					chats = message.chats.map {
						State.ChatState(chat = it, readMessages = listOf(), unreadMessages = listOf())
					}
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
			}
	}
}
