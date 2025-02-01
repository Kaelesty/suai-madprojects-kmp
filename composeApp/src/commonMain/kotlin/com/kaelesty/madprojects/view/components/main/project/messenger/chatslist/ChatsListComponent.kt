package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface ChatsListComponent {

	val state: StateFlow<MessengerStore.State>

	fun onChatSelected(chatId: Int)

	interface Navigator {

		fun toChat(chatId: Int)
	}

	interface Factory {

		fun create(
			componentContext: ComponentContext,
			navigator: Navigator,
			store: MessengerStore,
		): ChatsListComponent
	}
}

class DefaultChatListComponent(
	componentContext: ComponentContext,
	private val store: MessengerStore,
	private val navigator: ChatsListComponent.Navigator
): ComponentContext by componentContext, ChatsListComponent {

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<MessengerStore.State>
		get() = store.stateFlow

	override fun onChatSelected(chatId: Int) {
		navigator.toChat(chatId)
	}
}