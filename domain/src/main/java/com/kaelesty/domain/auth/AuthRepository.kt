package com.kaelesty.domain.auth

import com.kaelesty.domain.auth.login.LoginBadRequestErrors
import com.kaelesty.domain.auth.login.LoginBody
import com.kaelesty.domain.common.UseCaseResult

interface AuthRepository {

	suspend fun login(email: String, password: String): UseCaseResult<LoginBody, LoginBadRequestErrors>
}