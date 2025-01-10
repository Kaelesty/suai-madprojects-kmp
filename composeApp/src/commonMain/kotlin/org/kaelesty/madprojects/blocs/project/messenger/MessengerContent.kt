package com.kaelesty.madprojects_kmp.blocs.project.messenger

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chat.ChatContent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.ChatListContent

@Composable
fun MessengerContent(
	component: MessengerComponent,
	onChatShown: (Boolean) -> Unit
) {

	Children(stack = component.stack) {

		onChatShown(it.instance !is MessengerComponent.Child.Chat)

		when (val instance = it.instance) {
			is MessengerComponent.Child.Chat -> ChatContent(instance.component)
			is MessengerComponent.Child.ChatsList -> ChatListContent(instance.component)
		}
	}
}