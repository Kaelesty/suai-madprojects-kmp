package com.kaelesty.data.auth.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val id: String,
    val userName: String,
    val email: String,
    val token: String,
)