package com.kaelesty.data.remote.auth

import com.kaelesty.domain.auth.UserType
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val lastName: String,
    val firstName: String,
    val secondName: String,
    val data: String, // group for common, grade for curator
    val email: String,
    val password: String,
    val userType: UserType,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthorizedResponse(
    val refreshToken: String,
    val accessToken: String,
    val userType: UserType
)