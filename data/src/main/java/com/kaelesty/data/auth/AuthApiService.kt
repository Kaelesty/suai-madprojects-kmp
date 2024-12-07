package com.kaelesty.data.auth

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET

interface AuthApiService {

    @GET("/account/register")
    fun register(
        @Body body: RegisterRequest
    ): RegisterResponse
}