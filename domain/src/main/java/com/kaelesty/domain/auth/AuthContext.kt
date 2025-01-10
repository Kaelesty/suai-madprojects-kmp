package com.kaelesty.domain.auth

data class AuthContext(
    val userType: UserType,
    val jwt: String
)