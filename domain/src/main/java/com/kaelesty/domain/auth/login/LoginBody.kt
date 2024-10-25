package com.kaelesty.domain.auth.login

import entities.UserType


data class LoginBody(
	val userType: UserType,
	val jwt: String,
)
