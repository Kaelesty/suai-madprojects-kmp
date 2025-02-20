package com.kaelesty.madprojects.domain.repos.sprints

import com.kaelesty.madprojects.domain.repos.socket.KanbanState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SprintView(
    @SerialName("meta") val meta: SprintMeta,
    @SerialName("kanban") val kanban: KanbanState
)

@Serializable
data class SprintMeta(
    @SerialName("id") val id: String,
    @SerialName("startDate") val startDate: String,
    @SerialName("actualEndDate") val actualEndDate: String?,
    @SerialName("supposedEndDate") val supposedEndDate: String,
    @SerialName("title") val title: String,
    @SerialName("desc") val desc: String,
)