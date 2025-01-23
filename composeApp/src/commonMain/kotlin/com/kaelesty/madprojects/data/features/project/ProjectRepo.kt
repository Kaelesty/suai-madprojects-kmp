package com.kaelesty.madprojects.data.features.project

import com.kaelesty.madprojects.data.UnauthorizedException
import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.repos.project.ProjectRepo
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class ProjectRepoImpl(
    private val projectApiService: ProjectApiService,
    private val loginManager: LoginManager,
): ProjectRepo {

    override suspend fun validateRepolink(repolink: String): Boolean {
        return projectApiService.validateRepolink(
            token = loginManager.getTokenOrThrow(),
            repolink = repolink
        ).getOrNull()?.let {
            it.status == HttpStatusCode.OK
        } ?: false
    }

    override suspend fun createProject(
        title: String,
        maxMembersCount: Int,
        desc: String,
        curatorId: String,
        repoLinks: List<String>,
        projectGroupId: String,
    ): Result<String> {
        return kotlin.runCatching {
            projectApiService.createProject(
                token = loginManager.getTokenOrThrow(),
                title = title,
                maxMembersCount = maxMembersCount,
                desc = desc,
                curatorId = curatorId,
                repoLinks = repoLinks,
                projectGroupId = projectGroupId
            ).let {
                it.getOrThrow().let {
                    when (it.status) {
                        HttpStatusCode.OK -> {
                            it.body<CreateProjectResponse>().projectId
                        }
                        HttpStatusCode.Unauthorized -> {
                            loginManager.unauthorize()
                            throw UnauthorizedException
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                }
            }
        }
    }
}