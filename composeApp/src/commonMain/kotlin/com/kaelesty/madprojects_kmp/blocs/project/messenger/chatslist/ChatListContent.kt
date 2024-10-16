package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ChatListContent(
	component: ChatsListComponent
) {
	val state by component.state.collectAsState()

	Column {
		state.chats.forEach {
			Text(it.chat.title)
		}
	}
}