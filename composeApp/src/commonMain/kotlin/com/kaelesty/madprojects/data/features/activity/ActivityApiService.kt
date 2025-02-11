package com.kaelesty.madprojects.data.features.activity

import com.kaelesty.madprojects.data.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ActivityApiService(
    private val httpClient: HttpClient,
) {

    suspend fun getActivity(token: String, projectId: String, count: Int?) = kotlin.runCatching {
        httpClient.get("$BASE_URL/project/activity/get") {
            bearerAuth(token)
            parameter("projectId", projectId)
            if (count != null) parameter("count", count)
        }
    }
}