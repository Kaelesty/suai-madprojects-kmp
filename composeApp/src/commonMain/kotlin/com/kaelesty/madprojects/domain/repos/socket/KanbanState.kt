package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("KanbanState")
data class KanbanState(
    @SerialName("columns") val columns: List<Column>
) {

    @Serializable
    @SerialName("Column")
    data class Column(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("kards") val kards: List<Kard>,
        @SerialName("color") val color: String,
    )

    @Serializable
    @SerialName("Kard")
    data class Kard(
        @SerialName("id") val id: Int,
        @SerialName("authorName") val authorName: String,
        @SerialName("createdTimeMillis") val createdTimeMillis: Long,
        @SerialName("updateTimeMillis") val updateTimeMillis: Long,
        @SerialName("title") val title: String,
        @SerialName("desc") val desc: String,
        @SerialName("chatId") val chatId: String?,
        @SerialName("unreadMessage") val unreadMessage: Int?,
    )
}