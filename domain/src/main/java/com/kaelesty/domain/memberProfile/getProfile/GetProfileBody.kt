package com.kaelesty.domain.memberProfile.getProfile

import kotlinx.serialization.Serializable

@Serializable
data class GetProfileBody(
    val userName: String,
    val avatarUrl: String,
    val group: String,
    val githubLink: String,
    val email: String,
    val projects: List<ProjectBody>,
) {
    @Serializable
    data class ProjectBody(
        val id: Int,
        val name: String,
    )
}