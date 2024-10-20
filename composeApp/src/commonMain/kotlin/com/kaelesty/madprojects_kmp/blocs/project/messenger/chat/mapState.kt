package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import entities.Message

fun MessengerStore.State.toChatState(chatId: Int, userId: Int): ChatComponent.State {

    val BLOCK_TIME_WINDOW_MILLIS = 1000 * 60 * 5

    chats.first { it.chat.id == chatId }.let { chatState ->

        val sendersIds = mutableListOf<Int>()

        val readMessageBlocks: MutableList<ChatComponent.State.MessageBlock> = mutableListOf()
        val unreadMessageBlocks: MutableList<ChatComponent.State.MessageBlock> = mutableListOf()

        chatState.messages.forEach {
            if (it.senderId !in sendersIds) {
                sendersIds.add(it.senderId)
            }
            if (it.isRead) {
                val lastBlock = readMessageBlocks.lastOrNull()
                val diff = it.time - (lastBlock?.messages?.firstOrNull()?.time ?: -1L)
                if (
                    lastBlock?.senderId == it.senderId &&
                    diff < BLOCK_TIME_WINDOW_MILLIS
                ) {
                    readMessageBlocks.removeLast()
                    readMessageBlocks.add(
                        lastBlock.copy(
                            messages = lastBlock.messages
                                .toMutableList()
                                .apply {
                                    add(it)
                                }
                                .toList()
                        )
                    )
                } else {
                    readMessageBlocks.add(
                        ChatComponent.State.MessageBlock(
                            senderId = it.senderId,
                            messages = listOf(it),
                            type = if (userId == it.senderId) ChatComponent.State.MessageBlock.MessageBlockType.Outcoming
                            else ChatComponent.State.MessageBlock.MessageBlockType.Incoming
                        )
                    )
                }
            }
            else {
                val lastBlock = unreadMessageBlocks.lastOrNull()
                val diff = it.time - (lastBlock?.messages?.firstOrNull()?.time ?: -1L)
                if (
                    lastBlock?.senderId == it.senderId &&
                    diff < BLOCK_TIME_WINDOW_MILLIS
                ) {
                    unreadMessageBlocks.removeLast()
                    unreadMessageBlocks.add(
                        lastBlock.copy(
                            messages = lastBlock.messages
                                .toMutableList()
                                .apply {
                                    add(it)
                                }
                                .toList()
                        )
                    )
                } else {
                    unreadMessageBlocks.add(
                        ChatComponent.State.MessageBlock(
                            senderId = it.senderId,
                            messages = listOf(it),
                            type = if (userId == it.senderId) ChatComponent.State.MessageBlock.MessageBlockType.Outcoming
                            else ChatComponent.State.MessageBlock.MessageBlockType.Incoming
                        )
                    )
                }
            }
        }

        // Placeholder solution while there is no implementation of users on the main backend yet
        // TODO
        val senders = sendersIds.map {
            ChatComponent.State.MessageSender(
                id = it,
                avatarUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQa-Q-sLOhtUNX_f7Fo9UvrRVs8wStydNgI4TCVFbtbcERWqtz-QhBhhQIhuPyQRoUUUp8&usqp=CAU",
                name = "Sender $it"
            )
        }

        return ChatComponent.State(
            chatId = chatId,
            chatTitle = chatState.chat.title,
            senders = senders,
            readBlocks = readMessageBlocks,
            unreadBlocks = unreadMessageBlocks,
        )
    }
}