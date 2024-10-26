package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import entities.Chat
import entities.ChatType
import entities.Message
import kotlinx.coroutines.flow.StateFlow

interface ChatComponent {

	val state: StateFlow<State>

	data class State(
		val chatId: Int = -1,
		val chatTitle: String = "None",
		val senders: List<MessageSender> = listOf(),
		val readBlocks: List<MessageBlock> = listOf(),
		val unreadBlocks: List<MessageBlock> = listOf(),
	) {
		data class MessageBlock(
			val senderId: Int,
			val messages: List<Message>,
			val type: MessageBlockType,
		) {
			enum class MessageBlockType {
				Incoming, Outcoming,
			}
		}

		data class MessageSender(
			val name: String,
			val id: Int,
			val avatarUrl: String,
		)
	}

	fun sendMessage(text: String)

	fun readMessage(messageId: Int)
}