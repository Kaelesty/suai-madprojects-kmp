package com.kaelesty.domain.auth

import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface AuthenticationManager {

    @Serializable
    data class AuthenticationContext(
        val token: String,
        val username: String?, // TODO remove nullability
        val email: String,
        val id: String,
        val password: String,
        val firstName: String,
        val secondName: String,
        val lastName: String,
        val group: String,
    )

    suspend fun init()

    suspend fun login(
        email: String,
        password: String,
    ): LoginResult

    suspend fun register(
        username: String,
        firstName: String,
        secondName: String,
        lastName: String,
        group: String,
        email: String,
        password: String,
    ): RegisterResult

    suspend fun logout()

    val authorizedFlow: StateFlow<Boolean>

    suspend fun getContext(): AuthenticationContext?
}

enum class LoginResult {
    OK, ERROR // TODO
}

enum class RegisterResult {
    OK, ERROR
}