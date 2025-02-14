package com.kaelesty.madprojects.data.features.profile

import com.kaelesty.madprojects.data.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class ProfileApiService(
    private val httpClient: HttpClient
) {

    suspend fun getSharedProfile(token: String, userId: String): Result<HttpResponse> {
        return kotlin.runCatching {
            httpClient.get("$BASE_URL/sharedProfile") {
                bearerAuth(token)
                parameter("userId", userId)
            }
        }
    }

    suspend fun getCuratorProfile(token: String): HttpResponse? {
        return try {
            httpClient.get("$BASE_URL/curatorProfile") {
                bearerAuth(token)
            }
        }
        catch (e: Exception) {
            e.toString()
            null
        }
    }

    suspend fun getCommonProfile(token: String): HttpResponse? {
        return try {
            httpClient.get("$BASE_URL/commonProfile") {
                bearerAuth(token)
            }
        }
        catch (e: Exception) {
            e.toString()
            null
        }
    }
}