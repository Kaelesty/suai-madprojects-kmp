package com.kaelesty.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserMetaResponse(
    val githubId: Int,
    val githubAvatar: String,
    val profileLink: String,
    val id: String
)
