package com.kaelesty.madprojects.domain.repos.project

import kotlinx.serialization.Serializable

@Serializable
data class ProjectGroup(
    val id: String,
    val curatorId: String,
    val title: String
)