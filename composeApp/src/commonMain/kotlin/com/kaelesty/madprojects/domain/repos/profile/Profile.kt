package com.kaelesty.madprojects.domain.repos.profile

import com.kaelesty.madprojects.domain.repos.project.ProjectGroup
import com.kaelesty.madprojects.domain.repos.project.ProjectStatus
import kotlinx.serialization.Serializable

@Serializable
sealed interface Profile {

    @Serializable
    data class Common(
        val firstName: String,
        val lastName: String,
        val secondName: String,
        val email: String,
        val projects: List<ProfileProject>,
        val githubMeta: GithubUserMeta?,
        val group: String,
    ): Profile

    @Serializable
    data class Curator(
        val firstName: String,
        val secondName: String,
        val lastName: String,
        val email: String,
        val grade: String,
        val githubMeta: GithubUserMeta?,
        val projectGroups: List<ProjectGroup>
    ): Profile
}

@Serializable
data class ProfileProject(
    val id: String,
    val title: String,
    val mark: Int?,
    val status: ProjectStatus,
)