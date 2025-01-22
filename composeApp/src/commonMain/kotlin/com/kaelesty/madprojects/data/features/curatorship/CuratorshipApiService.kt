package com.kaelesty.madprojects.data.features.curatorship

import com.kaelesty.madprojects.data.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class CuratorshipApiService(
    private val httpClient: HttpClient
) {

    suspend fun getCuratorsList(token: String): Result<HttpResponse> {
        return kotlin.runCatching {
            httpClient.get("$BASE_URL/project/curators") {
                bearerAuth(token)
            }
        }
    }

    suspend fun getCuratorGroups(token: String, curatorId: String): Result<HttpResponse> {
        return kotlin.runCatching {
            httpClient.get("$BASE_URL/projectgroup/getCuratorGroups") {
                bearerAuth(token)
                parameter("curatorId", curatorId)
            }
        }
    }
}