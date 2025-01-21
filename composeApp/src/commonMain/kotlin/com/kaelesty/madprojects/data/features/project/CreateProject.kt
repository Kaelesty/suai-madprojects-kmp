package com.kaelesty.madprojects.data.features.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProject(
    @SerialName("title") val title: String,
    @SerialName("maxMembersCount") val maxMembersCount: Int,
    @SerialName("desc") val desc: String,
    @SerialName("curatorId") val curatorId: String,
    @SerialName("repoLinks") val repoLinks: List<String>,
    @SerialName("projectGroupId") val projectGroupId: String,
)

@Serializable
data class CreateProjectResponse(
    @SerialName("projectId") val projectId: String
)