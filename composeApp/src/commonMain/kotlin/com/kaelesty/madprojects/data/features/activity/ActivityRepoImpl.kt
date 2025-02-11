package com.kaelesty.madprojects.data.features.activity

import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.repos.story.ActivityRepo
import com.kaelesty.madprojects.domain.repos.story.ActivityResponse
import io.ktor.client.call.body

class ActivityRepoImpl(
    private val apiService: ActivityApiService,
    private val loginManager: LoginManager,
): ActivityRepo {

    override suspend fun getProjectActivity(projectId: Int, count: Int?): Result<ActivityResponse> {
        return kotlin.runCatching {
            apiService.getActivity(
                token = loginManager.getTokenOrThrow(),
                projectId = projectId.toString(),
                count = count
            ).getOrThrow().body<ActivityResponse>()
        }
    }
}