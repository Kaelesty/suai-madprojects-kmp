package com.kaelesty.domain.auth

class RegisterUseCase(
    private val repo: AuthRepo
) {

    suspend fun executeCommon(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        group: String,
        email: String,
        password: String
    ) = repo.registerCommon(
        username = username,
        lastName = lastName,
        firstName = firstName,
        secondName = secondName,
        group = group,
        email = email,
        password = password
    )

    suspend fun executeCurator(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        grade: String,
        email: String,
        password: String,
    ) = repo.registerCurator(
        username = username,
        lastName = lastName,
        firstName = firstName,
        secondName = secondName,
        grade = grade,
        email = email,
        password = password
    )
}