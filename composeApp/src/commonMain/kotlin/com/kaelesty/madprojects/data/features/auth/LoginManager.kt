package com.kaelesty.madprojects.data.features.auth

import com.kaelesty.data.remote.auth.AuthorizedResponse
import com.kaelesty.madprojects.data.UnauthorizedException
import com.kaelesty.madprojects.data.isExpired
import com.kaelesty.madprojects.data.local.PreferencesStorage
import com.kaelesty.madprojects.data.suspended
import com.kaelesty.madprojects.domain.repos.auth.LoginResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginManager(
    private val authApiService: AuthApiService,
    private val preferencesStorage: PreferencesStorage,
) {

    private val _authorization = MutableStateFlow<AuthorizedResponse?>(null)
    val authorization = _authorization.asStateFlow()

    suspend fun authorize(res: AuthorizedResponse) {
        _authorization.emit(res)
    }

    suspend fun unauthorize() {
        _authorization.emit(null)
        preferencesStorage.dropAuthResponse()
    }

    suspend fun getTokenOrThrow(): String {
        return _authorization.value?.let {
            if (!it.accessExpiresAt.isExpired()) {
                return@let it.accessToken
            }
            if (!it.refreshExpiresAt.isExpired()) {
                return@let refresh(it.refreshToken)
            }
            return@let null
        } ?: throw UnauthorizedException.suspended {
            unauthorize()
        }
    }

    private suspend fun refresh(token: String): String? {
        return authApiService.refresh(token)?.let {
            return if (it.status == HttpStatusCode.OK) {
                try {
                    it.body<AuthorizedResponse>().let {
                        authorize(it)
                        it.accessToken
                    }
                } catch (e: Exception) {
                    e.toString()
                    null
                }
            }
            else null
        }
    }

    fun installInterceptor(httpClient: HttpClient) {
        with(httpClient) {
            responsePipeline.intercept(HttpResponsePipeline.Receive) {
                if (this.context.response.status == HttpStatusCode.Unauthorized) {
                    unauthorize()
                }
            }
        }
    }
}