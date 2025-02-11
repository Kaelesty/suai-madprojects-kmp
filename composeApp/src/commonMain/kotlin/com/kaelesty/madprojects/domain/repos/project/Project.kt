package com.kaelesty.madprojects.domain.repos.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName("id") val id: String,
    @SerialName("meta") val meta: ProjectMeta,
    @SerialName("members") val members: List<ProjectMember>,
    @SerialName("repos") val repos: List<ProjectRepository>,
    @SerialName("isCreator") val isCreator: Boolean,
    @SerialName("status") val status: ProjectStatus,
    @SerialName("mark") val mark: Int?
)

@Serializable
data class ProjectMeta(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("desc") val desc: String,
    @SerialName("maxMembersCount") val maxMembersCount: Int,
    @SerialName("createDate") val createDate: String
)

@Serializable
data class ProjectMember(
    @SerialName("id") val id: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("secondName") val secondName: String,
)

@Serializable
data class ProjectRepository(
    @SerialName("id") val id: String,
    @SerialName("link") val link: String,
    @SerialName("title") val title: String,
)