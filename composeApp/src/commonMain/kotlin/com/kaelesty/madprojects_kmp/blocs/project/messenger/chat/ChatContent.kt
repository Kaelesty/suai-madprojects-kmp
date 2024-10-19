package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatContent(
	component: ChatComponent
) {

	val state by component.state.collectAsState()

	Column(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.padding(top = 8.dp)
	) {
		Text(
			text = state.chat.title,

		)

		LazyColumn {
			items(state.messages, key = { it.id }) {
				Text("${it.senderId}: ${it.text}")
			}
		}
	}
}