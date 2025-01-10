package com.kaelesty.domain.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthRepo {

    val isAuthorized: StateFlow<Boolean>

    suspend fun login(email: String, password: String): LoginResults

    suspend fun registerCommon(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        group: String,
        email: String,
        password: String,
    ): RegisterResults

    suspend fun registerCurator(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        grade: String,
        email: String,
        password: String
    ): RegisterResults

    suspend fun getContext(): AuthContext?

    suspend fun notifyUnauthorized()
}
