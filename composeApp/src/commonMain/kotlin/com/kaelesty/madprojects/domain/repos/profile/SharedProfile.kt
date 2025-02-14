package com.kaelesty.madprojects.domain.repos.profile

import com.kaelesty.madprojects.domain.UserType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedProfile(
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("lastName") val lastName: String,
)

@Serializable
data class SharedProfileResponse(
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("avatar") val avatar: String?,
    @SerialName("data") val data: String,
    @SerialName("githubLink") val githubLink: String?,
    @SerialName("email") val email: String,
    @SerialName("role") val role: UserType,
)