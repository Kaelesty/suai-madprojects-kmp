package com.kaelesty.madprojects.domain.repos.sprints

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSprint(
    @SerialName("id") val id: String,
    @SerialName("startDate") val startDate: String,
    @SerialName("actualEndDate") val actualEndDate: String?,
    @SerialName("supposedEndDate") val supposedEndDate: String,
    @SerialName("title") val title: String,
) {

    fun getTitleWithTime(): String {
        return "${title} (${actualEndDate ?: "Текущий"})"
    }
}