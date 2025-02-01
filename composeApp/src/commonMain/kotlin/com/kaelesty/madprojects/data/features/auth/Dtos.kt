package com.kaelesty.data.remote.auth

import com.kaelesty.madprojects.domain.UserType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("username") val username: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("data") val data: String, // group for common, grade for curator
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("userType") val userType: UserType,
)

@Serializable
data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

@Serializable
data class AuthorizedResponse(
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("accessToken") val accessToken: String,
    @SerialName("userType") val userType: UserType,
    @SerialName("accessExpiresAt") val accessExpiresAt: Long,
    @SerialName("refreshExpiresAt") val refreshExpiresAt: Long,
    @SerialName("userId") val userId: String,
)