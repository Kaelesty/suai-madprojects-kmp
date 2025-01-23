package com.kaelesty.madprojects.domain.repos.project

interface ProjectRepo {

    suspend fun createProject(
        title: String,
        maxMembersCount: Int,
        desc: String,
        curatorId: String,
        repoLinks: List<String>,
        projectGroupId: String,
    ): Result<String>

    suspend fun validateRepolink(
        repolink: String
    ): Boolean
}