package com.kaelesty.data.auth.apiService

import com.kaelesty.data.auth.dtos.AuthenticationResponse
import com.kaelesty.data.auth.dtos.LoginRequest
import com.kaelesty.data.auth.dtos.RegisterRequest
import de.jensklingenberg.ktorfit.http.Body

interface AuthApiService {

    suspend fun register(
        @Body body: RegisterRequest
    ): AuthenticationResponse

    suspend fun login(
        @Body body: LoginRequest
    ): AuthenticationResponse
}