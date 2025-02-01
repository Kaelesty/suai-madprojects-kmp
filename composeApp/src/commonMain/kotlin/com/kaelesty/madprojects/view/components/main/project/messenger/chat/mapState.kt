package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import com.kaelesty.madprojects.view.extensions.copyWith
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore

fun MessengerStore.State.toChatState(chatId: Int, userId: Int): ChatComponent.State {

    val BLOCK_TIME_WINDOW_MILLIS = 1000 * 60 * 5

    chats.first { it.chat.id == chatId }.let { chatState ->

        val sendersIds = mutableListOf<Int>()

        val readMessageBlocks: MutableList<ChatComponent.State.MessageBlock> = mutableListOf()
        val unreadMessageBlocks: MutableList<ChatComponent.State.MessageBlock> = mutableListOf()

        chatState.readMessages.forEach {
            if (it.senderId.toInt() !in sendersIds) {
                sendersIds.add(it.senderId.toInt())
            }
            val lastBlock = readMessageBlocks.lastOrNull()
            val diff = it.time - (lastBlock?.messages?.firstOrNull()?.time ?: -1L)
            if (
                lastBlock?.senderId == it.senderId.toInt() &&
                diff < BLOCK_TIME_WINDOW_MILLIS
            ) {
                readMessageBlocks.removeLast()
                readMessageBlocks.add(
                    lastBlock.copy(
                        messages = lastBlock.messages.copyWith(it)
                    )
                )
            } else {
                readMessageBlocks.add(
                    ChatComponent.State.MessageBlock(
                        senderId = it.senderId.toInt(),
                        messages = listOf(it),
                        type = if (userId == it.senderId.toInt()) ChatComponent.State.MessageBlock.MessageBlockType.Outcoming
                        else ChatComponent.State.MessageBlock.MessageBlockType.Incoming
                    )
                )
            }
        }
        chatState.unreadMessages.forEach {
            val lastBlock = unreadMessageBlocks.lastOrNull()
            val diff = it.time - (lastBlock?.messages?.firstOrNull()?.time ?: -1L)
            if (
                lastBlock?.senderId == it.senderId.toInt() &&
                diff < BLOCK_TIME_WINDOW_MILLIS
            ) {
                unreadMessageBlocks.removeLast()
                unreadMessageBlocks.add(
                    lastBlock.copy(
                        messages = lastBlock.messages
                            .copyWith(it)
                    )
                )
            } else {
                unreadMessageBlocks.add(
                    ChatComponent.State.MessageBlock(
                        senderId = it.senderId.toInt(),
                        messages = listOf(it),
                        type = if (userId == it.senderId.toInt()) ChatComponent.State.MessageBlock.MessageBlockType.Outcoming
                        else ChatComponent.State.MessageBlock.MessageBlockType.Incoming
                    )
                )
            }
        }

        val senders = senders.map {
            ChatComponent.State.MessageSender(
                id = it.id.toInt(),
                avatarUrl = it.avatar,
                name = "${it.firstName} ${it.lastName}"
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