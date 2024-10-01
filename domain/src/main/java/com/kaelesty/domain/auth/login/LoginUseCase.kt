package com.kaelesty.domain.auth.login

import com.kaelesty.domain.auth.AuthRepository

class LoginUseCase(
	private val repo: AuthRepository
) {
	suspend operator fun invoke(email: String, password: String) = repo.login(email, password)
}