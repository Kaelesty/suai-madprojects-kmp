package com.kaelesty.domain.auth

class LoginUseCase(
    private val repo: AuthRepo
) {

    suspend operator fun invoke(
        email: String, password: String,
    ) = repo.login(email, password)
}