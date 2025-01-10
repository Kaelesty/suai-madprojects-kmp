package com.kaelesty.data.remote.auth

import com.kaelesty.domain.auth.UserType
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

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
            httpClient.post("/auth/login") {
                setBody(
                    LoginRequest(
                        email = email, password = password
                    )
                )
            }
        }
        catch (e: Exception) {
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
            httpClient.post("/auth/register") {
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
            null
        }
    }
}