package com.kaelesty.data.auth.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val id: String,
    val userName: String?, // TODO remove nullability
    val email: String,
    val token: String,
    @SerialName("firstname") val firstName: String,
    @SerialName("secondname") val secondName: String,
    val lastName: String,
    val group: String,
    

)