package com.kaelesty.madprojects.data.features.curatorship

import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.repos.curatorship.AvailableCurator
import com.kaelesty.madprojects.domain.repos.curatorship.CuratorshipRepo
import com.kaelesty.madprojects.domain.repos.project.ProjectGroup
import io.ktor.client.call.body

class CuratorshipRepoImpl(
    private val curatorshipApiService: CuratorshipApiService,
    private val loginManager: LoginManager,
) : CuratorshipRepo {

    override suspend fun getCuratorProjectGroups(curatorId: String): Result<List<ProjectGroup>> {
        return kotlin.runCatching {
            curatorshipApiService.getCuratorGroups(
                token = loginManager.getTokenOrThrow(),
                curatorId = curatorId
            ).getOrThrow().body<List<ProjectGroup>>()
        }
    }

    override suspend fun getCuratorsList(): Result<List<AvailableCurator>> {
        return kotlin.runCatching {
            curatorshipApiService.getCuratorsList(
                token = loginManager.getTokenOrThrow()
            ).getOrThrow().body<List<AvailableCurator>>()
        }
    }
}