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
	}

	data class State(
		val chats: List<ChatState>
	) {
		data class ChatState(
			val chat: Chat,
			val messages: List<Message>
		)
	}

	sealed interface Label {
		data class NavigateToChat(val chatId: Int): Label
	}
}

class MessengerStoreFactory(
	private val storeFactory: StoreFactory,
	private val messenger: Messenger,
) {

	fun create(): MessengerStore =
		object : MessengerStore, Store<Intent, State, Label> by storeFactory.create(
			name = "MessengerStore",
			initialState = State(listOf()),
			bootstrapper = BootstrapperImpl(messenger),
			executorFactory = { ExecutorImpl(messenger) },
			reducer = ReducerImpl
		) {}

	private sealed interface Msg {
		data class AddChat(val chat: Chat) : Msg
		data class AddMessage(val message: Message, val chatId: Int) : Msg
		data class SetChatMessages(val messages: List<Message>, val chatId: Int) : Msg
		data class SetChatsList(val chats: List<Chat>) : Msg
	}

	private class BootstrapperImpl(
		private val messenger: Messenger
	) : CoroutineBootstrapper<ServerAction>() {
		override fun invoke() {
			scope.launch {
				messenger.connect {
					scope.launch {
						messenger.acceptAction(
							ClientAction.Authorize(
								jwt = "1",
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
					scope.launch {
						messenger.acceptAction(
							ClientAction.SendMessage(
								message = intent.text,
								chatId = intent.chatId)
						)
					}
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
					Msg.SetChatMessages(messages = action.messages, chatId = action.chatId)
				)

				is ServerAction.SendChatsList -> dispatch(
					Msg.SetChatsList(chats = action.chats)
				)
			}
		}
	}

	private object ReducerImpl : Reducer<State, Msg> {
		override fun State.reduce(message: Msg): State =
			when (message) {
				is Msg.AddChat -> copy(
					chats = chats
						.toMutableList()
						.apply {
							add(
								State.ChatState(
									chat = message.chat,
									messages = listOf()
								)
							)
						}
						.toList()
				)

				is Msg.AddMessage -> copy(
					chats = chats
						.map {
							if (it.chat.id == message.chatId) {
								it.copy(
									messages = it.messages
										.toMutableList()
										.apply {
											add(message.message)
										}
										.toList()
								)
							}
							else it
						}
				)
				is Msg.SetChatMessages -> copy(
					chats = chats
						.map {
							if (it.chat.id == message.chatId) {
								it.copy(
									messages = message.messages
								)
							}
							else it
						}
				)
				is Msg.SetChatsList -> copy(
					chats = message.chats.map {
						State.ChatState(chat = it, messages = listOf())
					}
				)
			}
	}
}
