package com.kaelesty.madprojects.domain.repos.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedProfile(
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("lastName") val lastName: String,
)