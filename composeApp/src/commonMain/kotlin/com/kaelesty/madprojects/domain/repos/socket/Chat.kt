package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Chat")
data class Chat(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("lastMessage") val lastMessage: Message?,
    @SerialName("unreadMessagesCount") val unreadMessagesCount: Int,
    @SerialName("chatType") val chatType: ChatType
)