package com.kaelesty.madprojects.data.features.github

import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.repos.github.BranchCommits
import com.kaelesty.madprojects.domain.repos.github.GithubRepo
import com.kaelesty.madprojects.domain.repos.github.RepoView
import io.ktor.client.call.body

class GithubRepoImpl(
    private val apiService: GithubApiService,
    private val loginManager: LoginManager,
): GithubRepo {

    override suspend fun getRepoBranchContent(repoName: String, sha: String): Result<BranchCommits> {
        return kotlin.runCatching {
            apiService.getProjectRepoBranchContent(
                token = loginManager.getTokenOrThrow(),
                repoName, sha
            ).getOrThrow().body<BranchCommits>()
        }
    }

    override suspend fun getProjectRepoBranches(projectId: String): Result<List<RepoView>> {
        return kotlin.runCatching {
            apiService.getProjectRepoBranches(
                token = loginManager.getTokenOrThrow(),
                projectId = projectId
            ).getOrThrow().body<List<RepoView>>()
        }
    }
}