package com.kaelesty.madprojects.data.remote.auth

import com.kaelesty.data.remote.auth.LoginRequest
import com.kaelesty.data.remote.auth.RegisterRequest
import com.kaelesty.madprojects.data.BASE_URL
import com.kaelesty.madprojects.domain.UserType
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiService(
    private val httpClient: HttpClient
) {

    suspend fun refresh(token: String): HttpResponse? {
        return try {
            httpClient.post("auth/refresh") {
                header("Autorization", "Bearer $token")
            }
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun login(email: String, password: String): HttpResponse? {
        return try {
            httpClient.post("$BASE_URL/auth/login") {
                setBody(
                    LoginRequest(
                        email = email, password = password
                    )
                )
                contentType(ContentType.Application.Json)
            }
        }
        catch (e: Exception) {
            e.toString()
            null
        }
    }

    suspend fun register(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        data: String,
        email: String,
        password: String,
        userType: UserType
    ): HttpResponse? {
        return try {
            httpClient.post("$BASE_URL/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        username = username,
                        lastName = lastName,
                        firstName = firstName,
                        secondName = secondName,
                        data = data,
                        email = email,
                        password = password,
                        userType = userType
                    )
                )
            }
        }
        catch (e: Exception) {
            e.toString()
            null
        }
    }
}