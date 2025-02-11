package com.kaelesty.madprojects.domain.repos.github

import com.kaelesty.madprojects.domain.repos.profile.GithubUserMeta
import com.kaelesty.madprojects.domain.repos.profile.SharedProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BranchCommits(
    @SerialName("commits") val commits: List<BranchCommitView>,
    @SerialName("authors") val authors: List<Commiter>
)

@Serializable
data class BranchCommitView(
    @SerialName("sha") val sha: String,
    @SerialName("authorGithubId") val authorGithubId: Int,
    @SerialName("date") val date: String,
    @SerialName("message") val message: String,
)

@Serializable
data class Commiter(
    @SerialName("profile") val profile: SharedProfile?,
    @SerialName("githubMeta") val githubMeta: GithubUserMeta?,
)