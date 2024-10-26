package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultChatListComponent(
	componentContext: ComponentContext,
	private val store: MessengerStore,
	private val onChatSelected_: (Int) -> Unit,
): ComponentContext by componentContext, ChatsListComponent {

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<MessengerStore.State>
		get() = store.stateFlow

	override fun onChatSelected(chatId: Int) {
		onChatSelected_(chatId)
	}
}