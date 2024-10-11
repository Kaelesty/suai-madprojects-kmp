package com.kaelesty.domain.auth.register

import com.kaelesty.domain.common.UserType

data class RegisterBody(
	val userType: UserType
)
