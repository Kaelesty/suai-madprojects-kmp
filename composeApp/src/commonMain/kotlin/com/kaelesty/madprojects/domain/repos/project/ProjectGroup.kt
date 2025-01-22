package com.kaelesty.madprojects.domain.repos.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectGroup(
    @SerialName("id") val id: String,
    @SerialName("curatorId") val curatorId: String,
    @SerialName("title") val title: String
)