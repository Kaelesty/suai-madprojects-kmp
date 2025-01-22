package com.kaelesty.madprojects.domain.repos.curatorship

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableCurator(
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
)