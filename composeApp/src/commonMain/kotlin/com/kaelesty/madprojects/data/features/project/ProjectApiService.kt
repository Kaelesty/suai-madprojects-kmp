package com.kaelesty.madprojects.data.features.project

import com.kaelesty.madprojects.data.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProjectApiService(
    private val httpClient: HttpClient
) {

    suspend fun createProject(
        token: String,
        title: String,
        maxMembersCount: Int,
        desc: String,
        curatorId: String,
        repoLinks: List<String>,
        projectGroupId: String,
    ): Result<HttpResponse> {
        return kotlin.runCatching {
            httpClient.post("$BASE_URL/project/create") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(
                    CreateProject(
                        title = title,
                        maxMembersCount = maxMembersCount,
                        desc = desc,
                        curatorId = curatorId,
                        repoLinks = repoLinks,
                        projectGroupId = projectGroupId
                    )
                )
            }
        }
    }
}