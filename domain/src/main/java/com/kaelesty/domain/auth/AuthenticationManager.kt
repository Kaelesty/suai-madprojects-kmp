package com.kaelesty.domain.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthenticationManager {

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
}

enum class LoginResult {
    OK, ERROR // TODO
}

enum class RegisterResult {
    OK, ERROR
}