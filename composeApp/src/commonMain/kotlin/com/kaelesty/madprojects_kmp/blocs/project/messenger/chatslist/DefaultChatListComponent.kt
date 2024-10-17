package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import kotlinx.coroutines.flow.StateFlow

class DefaultChatListComponent(
	componentContext: ComponentContext,
	private val store: MessengerStore
): ComponentContext by componentContext, ChatsListComponent {

	override val state: StateFlow<MessengerStore.State>
		get() = store.stateFlow

	override fun onChatSelected(chatId: Int) {
		TODO("Not yet implemented")
	}
}