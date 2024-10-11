package com.kaelesty.domain.auth.register

import com.kaelesty.domain.auth.AuthRepository
import com.kaelesty.domain.common.UserType

class RegisterUseCase(
	private val repo: AuthRepository
) {
	suspend operator fun invoke(email: String, password: String, userType: UserType)
		= repo.register(email, password, userType)
}