package com.kaelesty.madprojects.domain.repos.sprints

interface SprintsRepo {

    suspend fun getProjectSprints(projectId: String): Result<List<ProfileSprint>>


}