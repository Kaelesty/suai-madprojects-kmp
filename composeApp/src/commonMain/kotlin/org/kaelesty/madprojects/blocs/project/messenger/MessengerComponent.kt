package com.kaelesty.madprojects_kmp.blocs.project.messenger

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chat.ChatComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.ChatsListComponent
import kotlinx.coroutines.flow.StateFlow

interface MessengerComponent {

	val stack: Value<ChildStack<*, Child>>

	val state: StateFlow<MessengerStore.State>

	sealed interface Child {

		class ChatsList(val component: ChatsListComponent): Child

		class Chat(val component: ChatComponent): Child
	}
}