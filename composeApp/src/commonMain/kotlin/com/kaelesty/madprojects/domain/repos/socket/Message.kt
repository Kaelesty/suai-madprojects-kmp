package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Message")
data class Message(
    @SerialName("id") val id: Int,
    @SerialName("text") val text: String,
    @SerialName("senderId") val senderId: String,
    @SerialName("time") val time: Long,
    @SerialName("isRead") val isRead: Boolean = true
)