package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import entities.Chat
import entities.ChatType
import entities.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultChatComponent(
	private val componentContext: ComponentContext,
	private val store: MessengerStore,
	private val chatId: Int
): ComponentContext by componentContext, ChatComponent {

	private val scope = CoroutineScope(Dispatchers.IO)

	init {
		lifecycle.subscribe(
			object : Lifecycle.Callbacks {
				override fun onCreate() {
					super.onCreate()
					scope.launch {
						store.stateFlow.collect { newState ->
							_state.emit(
								newState.toChatState(chatId = chatId, userId = 1)
							)
						}
					}
				}
				override fun onDestroy() {
					super.onDestroy()
					scope.cancel()
				}
			}
		)
	}

	private val _state: MutableStateFlow<ChatComponent.State> = MutableStateFlow(ChatComponent.State())
	override val state: StateFlow<ChatComponent.State>
		get() = _state

	override fun sendMessage(text: String) {
		store.accept(MessengerStore.Intent.SendMessage(text, chatId))
	}
}