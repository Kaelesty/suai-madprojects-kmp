package com.kaelesty.data.auth

data class RegisterRequest(
    val username: String,
    val firstName: String,
    val secondName: String,
    val lastName: String,
    val group: String,
    val email: String,
    val password: String,
)

data class RegisterResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
)
