package com.kaelesty.madprojects.data.features.sprints

import com.kaelesty.madprojects.data.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SprintsApiService(
    private val httpClient: HttpClient
) {

    suspend fun getSprint(
        token: String,
        sprintId: String,
    ) = kotlin.runCatching {
        httpClient.get("$BASE_URL/sprint/get") {
            parameter("sprintId", sprintId)
            bearerAuth(token)
        }
    }

    suspend fun getProjectSprints(
        token: String,
        projectId: String
    ) = kotlin.runCatching {
        httpClient.get("$BASE_URL/sprint/getListByProject") {
            parameter("projectId", projectId)
            bearerAuth(token)
        }
    }
}