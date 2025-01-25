package com.kaelesty.madprojects.domain.repos.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoView(
    @SerialName("name") val name: String,
    @SerialName("repoBranches") val repoBranches: List<RepoBranchView>,
)

@Serializable
data class RepoBranchView(
    @SerialName("name") val name: String,
    @SerialName("sha") val sha: String,
)