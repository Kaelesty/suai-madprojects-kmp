package com.kaelesty.domain.auth

import com.kaelesty.domain.auth.login.LoginBadRequestErrors
import com.kaelesty.domain.auth.login.LoginBody
import com.kaelesty.domain.auth.register.RegisterBadRequestErrors
import com.kaelesty.domain.auth.register.RegisterBody
import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.common.UserType

interface AuthRepository {

	suspend fun login(email: String, password: String)
	: UseCaseResult<LoginBody, LoginBadRequestErrors>

	suspend fun register(email: String, password: String, userType: UserType)
	: UseCaseResult<RegisterBody, RegisterBadRequestErrors>
}