package com.kaelesty.madprojects.data.repos.auth

import com.kaelesty.data.remote.auth.AuthorizedResponse
import com.kaelesty.madprojects.data.remote.auth.AuthApiService
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects.domain.repos.auth.LoginResult
import com.kaelesty.madprojects.domain.stores.root.User
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepoImpl(
    private val apiService: AuthApiService,
) : AuthRepo {

    private val _user = MutableStateFlow<User?>(null)
    override fun getUser(): StateFlow<User?> = _user.asStateFlow()

    override suspend fun login(email: String, password: String): LoginResult {
        return apiService.login(email, password)?.let {
            return when (it.status) {
                HttpStatusCode.OK -> {
                    try {
                        it.body<AuthorizedResponse>().let {
                            onAuthorized(it)
                            LoginResult.OK
                        }
                    } catch (e: Exception) {
                        LoginResult.ERROR
                    }
                }

                else -> LoginResult.BAD_CREDENTIALS
            }
        } ?: LoginResult.ERROR
    }

    private suspend fun onAuthorized(res: AuthorizedResponse) {
        _user.emit(
            User(
                type = res.userType
            )
        )

        // TODO save JWTs
    }
}