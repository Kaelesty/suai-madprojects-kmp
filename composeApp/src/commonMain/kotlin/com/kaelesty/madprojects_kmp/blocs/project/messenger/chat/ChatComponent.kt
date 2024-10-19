package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import entities.Chat
import entities.ChatType
import entities.Message
import kotlinx.coroutines.flow.StateFlow

interface ChatComponent {

	val state: StateFlow<MessengerStore.State.ChatState>
}