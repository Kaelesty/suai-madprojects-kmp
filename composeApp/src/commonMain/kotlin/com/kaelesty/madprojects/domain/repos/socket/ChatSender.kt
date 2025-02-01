package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ChatSender")
data class ChatSender(
    @SerialName("id") val id: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("avatar") val avatar: String?
)