package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ChatType")
enum class ChatType {
    @SerialName("Public") Public, @SerialName("MembersPrivate") MembersPrivate,
    @SerialName("CuratorsPrivate") CuratorPrivate, @SerialName("Kard") Kard
}