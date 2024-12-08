package com.kaelesty.data.auth.store

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationContext(
    val token: String,
    val username: String,
    val email: String,
    val id: String,
    val password: String,
)

interface AuthenticationStore {

    suspend fun get(): AuthenticationContext?

    suspend fun save(new: AuthenticationContext)

    suspend fun updateToken(new: String)

    suspend fun drop()
}