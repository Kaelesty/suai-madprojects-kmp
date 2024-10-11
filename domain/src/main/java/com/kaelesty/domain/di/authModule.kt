package com.kaelesty.domain.di

import com.kaelesty.domain.auth.AuthRepository
import com.kaelesty.domain.auth.login.LoginBadRequestErrors
import com.kaelesty.domain.auth.login.LoginBody
import com.kaelesty.domain.auth.login.LoginUseCase
import com.kaelesty.domain.auth.register.RegisterBadRequestErrors
import com.kaelesty.domain.auth.register.RegisterBody
import com.kaelesty.domain.auth.register.RegisterUseCase
import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.common.UserType
import kotlinx.coroutines.delay
import org.koin.dsl.module

val authModule = module {

	factory<AuthRepository> {
		object : AuthRepository {
			override suspend fun login(
				email: String,
				password: String
			): UseCaseResult<LoginBody, LoginBadRequestErrors> {
				return UseCaseResult.Success(
					body = LoginBody(
						userType = UserType.STUDENT
					)
				)
			}

			override suspend fun register(
				email: String,
				password: String,
				userType: UserType
			): UseCaseResult<RegisterBody, RegisterBadRequestErrors> {
				delay(5000)
				return UseCaseResult.BadRequest(RegisterBadRequestErrors.USED_EMAIL)
			}
		}
	}

	factory<LoginUseCase> {
		LoginUseCase(
			repo = get()
		)
	}

	factory<RegisterUseCase> {
		RegisterUseCase(
			repo = get()
		)
	}
}