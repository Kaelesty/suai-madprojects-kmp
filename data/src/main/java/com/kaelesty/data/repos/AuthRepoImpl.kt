package com.kaelesty.data.repos

import com.kaelesty.data.local.AuthPreferencesStore
import com.kaelesty.data.remote.auth.AuthApiService
import com.kaelesty.data.remote.auth.AuthorizedResponse
import com.kaelesty.data.remote.auth.accessTokenLifetime
import com.kaelesty.data.remote.auth.lag
import com.kaelesty.data.remote.auth.refreshTokenLifetime
import com.kaelesty.domain.auth.AuthContext
import com.kaelesty.domain.auth.AuthRepo
import com.kaelesty.domain.auth.LoginResults
import com.kaelesty.domain.auth.RegisterResults
import com.kaelesty.domain.auth.UserType
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepoImpl(
    private val apiService: AuthApiService,
    private val authPreferencesStore: AuthPreferencesStore
): AuthRepo {

    private val _isAuthorized = MutableStateFlow(false)
    override val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    override suspend fun login(email: String, password: String): LoginResults {
        apiService.login(email, password)?.let {
            return when (it.status) {
                HttpStatusCode.OK -> {
                    try {
                        it.body<AuthorizedResponse>().let {
                            onAuthorized(it)
                            LoginResults.OK
                        }
                    }
                    catch (e: Exception) {
                        LoginResults.INTERNET_ERROR
                    }
                    LoginResults.OK
                }
                else -> LoginResults.BAD_REQUEST
            }
        }
        return LoginResults.INTERNET_ERROR
    }

    override suspend fun registerCommon(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        group: String,
        email: String,
        password: String
    ): RegisterResults {
        apiService.register(
            username = username,
            lastName = lastName,
            firstName = firstName,
            secondName = secondName,
            data = group,
            email = email,
            password = password,
            userType = UserType.Common
        )?.let {
            return when (it.status) {
                HttpStatusCode.OK -> {
                    try {
                        it.body<AuthorizedResponse>().let {
                            onAuthorized(it)
                        }
                        RegisterResults.OK
                    }
                    catch (e: Exception) {
                        RegisterResults.BAD_REQUEST
                    }
                }
                HttpStatusCode.Conflict -> RegisterResults.BAD_EMAIL
                HttpStatusCode.NotAcceptable -> RegisterResults.BAD_USERNAME
                HttpStatusCode.Forbidden -> RegisterResults.BAD_PASSWORD
                else -> RegisterResults.BAD_REQUEST
            }
        }
        return RegisterResults.INTERNET_ERROR
    }

    override suspend fun registerCurator(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        grade: String,
        email: String,
        password: String
    ): RegisterResults {
        apiService.register(
            username = username,
            lastName = lastName,
            firstName = firstName,
            secondName = secondName,
            data = grade,
            email = email,
            password = password,
            userType = UserType.Common
        )?.let {
            return when (it.status) {
                HttpStatusCode.OK -> {
                    try {
                        it.body<AuthorizedResponse>().let {
                            onAuthorized(it)
                        }
                        RegisterResults.OK
                    }
                    catch (e: Exception) {
                        RegisterResults.BAD_REQUEST
                    }
                }
                HttpStatusCode.Conflict -> RegisterResults.BAD_EMAIL
                HttpStatusCode.NotAcceptable -> RegisterResults.BAD_USERNAME
                HttpStatusCode.Forbidden -> RegisterResults.BAD_PASSWORD
                else -> RegisterResults.BAD_REQUEST
            }
        }
        return RegisterResults.INTERNET_ERROR
    }

    override suspend fun getContext(): AuthContext? {
        authPreferencesStore.getAuthorization()?.let {

            if (it.expiresAt < System.currentTimeMillis() + lag) {
                return AuthContext(
                    userType = it.userType, jwt = it.jwt
                )
            }

            authPreferencesStore.getRefresh()?.let {
                if (it.expiresAt < System.currentTimeMillis() + lag) {
                    apiService.refresh(it.jwt)?.let {
                        when (it.status) {
                            HttpStatusCode.OK -> {
                                try {
                                    it.body<AuthorizedResponse>().let {
                                        onAuthorized(it)
                                        return AuthContext(
                                            userType = it.userType,
                                            jwt = it.accessToken
                                        )
                                    }
                                }
                                catch (e: Exception) {
                                    null
                                }
                            }
                            else -> {
                                null
                            }
                        }
                    }
                }
            }
        }

        return null
    }

    override suspend fun notifyUnauthorized() {
        authPreferencesStore.drop()
        _isAuthorized.emit(false)
    }

    private suspend fun onAuthorized(data: AuthorizedResponse) {
        System.currentTimeMillis().let { time ->
            authPreferencesStore.save(
                refresh = data.refreshToken,
                access = data.accessToken,
                userType = data.userType,
                refreshExpires = time + refreshTokenLifetime,
                accessExpires = time + accessTokenLifetime,
            )
        }
        _isAuthorized.emit(true)
    }
}