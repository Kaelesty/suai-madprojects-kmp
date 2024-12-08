package com.kaelesty.data.auth.apiService

import com.kaelesty.data.auth.dtos.AuthenticationResponse
import com.kaelesty.data.auth.dtos.LoginRequest
import com.kaelesty.data.auth.dtos.RegisterRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiServiceImpl(
    private val client: HttpClient
): AuthApiService {

    private val baseUrl = "https://kaelesty.ru:5000/api"

    override suspend fun register(body: RegisterRequest): AuthenticationResponse {
        val response = client.post("$baseUrl/account/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body<AuthenticationResponse>()
    }

    override suspend fun login(body: LoginRequest): AuthenticationResponse {
        val response = client.post("$baseUrl/account/login") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body<AuthenticationResponse>()
    }
}