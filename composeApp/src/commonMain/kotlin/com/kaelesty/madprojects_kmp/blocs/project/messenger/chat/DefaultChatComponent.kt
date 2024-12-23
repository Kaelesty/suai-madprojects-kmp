package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultChatComponent(
	private val componentContext: ComponentContext,
	private val store: MessengerStore,
	private val chatId: Int
): ComponentContext by componentContext, ChatComponent {

	private val scope = CoroutineScope(Dispatchers.IO)

	private val messagesToRead: MutableStateFlow<MutableList<Int>?> = MutableStateFlow(null)

	init {
		lifecycle.subscribe(
			object : Lifecycle.Callbacks {
				override fun onCreate() {
					super.onCreate()
					scope.launch {
						store.stateFlow.collect { newState ->
							_state.emit(
								newState.toChatState(chatId = chatId, userId = 2)
							)
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

	private val _state: MutableStateFlow<ChatComponent.State> = MutableStateFlow(ChatComponent.State())
	override val state: StateFlow<ChatComponent.State>
		get() = _state

	override fun sendMessage(text: String) {
		store.accept(MessengerStore.Intent.SendMessage(text, chatId))
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