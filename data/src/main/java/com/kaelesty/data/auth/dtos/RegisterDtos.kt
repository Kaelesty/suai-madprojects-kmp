package com.kaelesty.data.auth.dtos

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val firstName: String,
    val secondName: String,
    val lastName: String,
    val group: String,
    val email: String,
    val password: String,
)
