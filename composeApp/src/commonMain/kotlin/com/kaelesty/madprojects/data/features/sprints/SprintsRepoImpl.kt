package com.kaelesty.madprojects.data.features.sprints

import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.repos.sprints.ProfileSprint
import com.kaelesty.madprojects.domain.repos.sprints.SprintsRepo
import io.ktor.client.call.body

class SprintsRepoImpl(
    private val apiService: SprintsApiService,
    private val loginManager: LoginManager,
) : SprintsRepo {

    override suspend fun getProjectSprints(projectId: String): Result<List<ProfileSprint>> {
        return kotlin.runCatching {
            apiService.getProjectSprints(
                token = loginManager.getTokenOrThrow(),
                projectId = projectId
            ).getOrThrow().body<List<ProfileSprint>>()
        }
    }
}