package com.kaelesty.madprojects.data.repos.auth

import com.kaelesty.data.remote.auth.AuthorizedResponse
import com.kaelesty.madprojects.data.local.PreferencesStorage
import com.kaelesty.madprojects.data.remote.auth.AuthApiService
import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects.domain.repos.auth.LoginResult
import com.kaelesty.madprojects.domain.repos.auth.RegisterResult
import com.kaelesty.madprojects.domain.stores.root.User
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthRepoImpl(
    private val apiService: AuthApiService,
    private val preferencesStorage: PreferencesStorage
) : AuthRepo {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _user = MutableStateFlow<User?>(null)
    override fun getUser(): StateFlow<User?> = _user.asStateFlow()

    init {
        scope.launch {
            checkAuthorizedOnLaunch()
        }
    }

    override suspend fun register(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        data: String,
        email: String,
        password: String,
        userType: UserType
    ): RegisterResult {
        return apiService.register(
            username = username,
            lastName = lastName,
            firstName = firstName,
            secondName = secondName,
            data = data,
            email = email,
            password = password,
            userType = userType
        )?.let {
            return when (it.status) {
                HttpStatusCode.OK -> {
                    try {
                        it.body<AuthorizedResponse>().let {
                            onAuthorized(it)
                            RegisterResult.OK
                        }
                    } catch (e: Exception) {
                        e.toString()
                        RegisterResult.BAD_REQUEST
                    }
                }
                HttpStatusCode.Forbidden -> RegisterResult.BAD_PASSWORD
                HttpStatusCode.Conflict -> RegisterResult.BAD_EMAIL
                HttpStatusCode.NotAcceptable -> RegisterResult.BAD_USERNAME
                else -> RegisterResult.BAD_REQUEST
            }
        } ?: RegisterResult.INTERNET_ERROR
    }

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
                        e.toString()
                        LoginResult.ERROR
                    }
                }

                else -> LoginResult.BAD_CREDENTIALS
            }
        } ?: LoginResult.ERROR
    }

    private suspend fun onAuthorized(res: AuthorizedResponse) {
        preferencesStorage.saveAuthResponse(res)
        _user.emit(
            User(
                type = res.userType
            )
        )
    }

    private suspend fun checkAuthorizedOnLaunch() {
        preferencesStorage.getAuthResponse().collect {
            it?.let {
                _user.emit(
                    User(
                        type = it.userType
                    )
                )
            }
        }
    }
}