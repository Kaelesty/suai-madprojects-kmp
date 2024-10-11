package com.kaelesty.domain.auth.login

import com.kaelesty.domain.common.UserType

data class LoginBody(
	val userType: UserType
)
