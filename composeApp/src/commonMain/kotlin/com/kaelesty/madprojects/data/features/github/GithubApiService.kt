package com.kaelesty.madprojects.data.features.github

import com.kaelesty.madprojects.data.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GithubApiService(
    private val httpClient: HttpClient
) {

    suspend fun getProjectRepoBranches(
        token: String,
        projectId: String,
    ) = kotlin.runCatching {
        httpClient.get("$BASE_URL/github/getProjectRepoBranches") {
            parameter("projectId", projectId)
            bearerAuth(token)
        }
    }

    suspend fun getProjectRepoBranchContent(
        token: String,
        repoName: String,
        sha: String
    ) = kotlin.runCatching {
        httpClient.get("$BASE_URL/github/getRepoBranchContent") {
            parameter("sha", sha)
            parameter("repoName", repoName)
            bearerAuth(token)
        }
    }
}