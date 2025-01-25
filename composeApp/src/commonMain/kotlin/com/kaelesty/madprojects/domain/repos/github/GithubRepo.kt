package com.kaelesty.madprojects.domain.repos.github

interface GithubRepo {

    suspend fun getRepoBranchContent(repoName: String, sha: String): Result<BranchCommits>

    suspend fun getProjectRepoBranches(projectId: String): Result<List<RepoView>>
}