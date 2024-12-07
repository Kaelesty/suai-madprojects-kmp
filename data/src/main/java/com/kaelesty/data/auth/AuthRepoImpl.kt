package com.kaelesty.data.auth

import com.kaelesty.domain.auth.AuthRepository
import com.kaelesty.domain.auth.login.LoginBadRequestErrors
import com.kaelesty.domain.auth.login.LoginBody
import com.kaelesty.domain.auth.register.RegisterBadRequestErrors
import com.kaelesty.domain.auth.register.RegisterBody
import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.common.UserType
import io.ktor.client.HttpClient

class AuthRepoImpl(

): AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): UseCaseResult<LoginBody, LoginBadRequestErrors> {

    }

    override suspend fun register(
        email: String,
        password: String,
        userType: UserType
    ): UseCaseResult<RegisterBody, RegisterBadRequestErrors> {
        TODO("Not yet implemented")
    }
}