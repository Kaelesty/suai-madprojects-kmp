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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
								newState.chats.first { it.chat.id == chatId }
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

	private val _state: MutableStateFlow<MessengerStore.State.ChatState> = MutableStateFlow(
		MessengerStore.State.ChatState(
			Chat(0, "", null, 0, ChatType.Public),
			messages = listOf()
		)
	)
	override val state: StateFlow<MessengerStore.State.ChatState>
		get() = _state
}