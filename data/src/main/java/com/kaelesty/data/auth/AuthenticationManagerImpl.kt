package com.kaelesty.data.auth

import com.kaelesty.data.auth.apiService.AuthApiService
import com.kaelesty.data.auth.dtos.LoginRequest
import com.kaelesty.data.auth.dtos.RegisterRequest
import com.kaelesty.data.auth.store.AuthenticationStore
import com.kaelesty.domain.auth.AuthenticationManager
import com.kaelesty.domain.auth.LoginResult
import com.kaelesty.domain.auth.RegisterResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationManagerImpl(
    private val authApiService: AuthApiService,
    private val store: AuthenticationStore,
): AuthenticationManager {

    private val _authorizedFlow = MutableStateFlow(false)
    override val authorizedFlow: StateFlow<Boolean>
        get() = _authorizedFlow.asStateFlow()

    override suspend fun init() {
        val currentContext = store.get()
        if (currentContext != null) {
            if (!isTokenAlive(token = currentContext.token)) {
                login(
                    email = currentContext.email,
                    password = currentContext.password,
                )
            }
        }
    }

    override suspend fun getContext(): AuthenticationManager.AuthenticationContext? {
        return store.get()
    }

    private fun isTokenAlive(token: String): Boolean {
        // TODO
        return false
    }

    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            val response = authApiService.login(
                body = LoginRequest(
                    email = email, password = password
                )
            )

            store.save(
                new = AuthenticationManager.AuthenticationContext(
                    token = response.token,
                    username = response.userName,
                    email = email,
                    id = response.id,
                    password = password,
                    firstName = response.firstName,
                    secondName = response.secondName,
                    lastName = response.lastName,
                    group = response.group,
                )
            )
            _authorizedFlow.emit(true)
            return LoginResult.OK
        } catch (e: Exception) {
            LoginResult.ERROR
        }
    }

    override suspend  fun register(
        username: String,
        firstName: String,
        secondName: String,
        lastName: String,
        group: String,
        email: String,
        password: String
    ): RegisterResult {
        return try {
            val response = authApiService.register(
                body = RegisterRequest(
                    username = username,
                    firstName = firstName,
                    secondName = secondName,
                    lastName = lastName,
                    group = group,
                    email = email,
                    password = password
                )
            )

            store.save(
                new = AuthenticationManager.AuthenticationContext(
                    token = response.token,
                    username = username,
                    email = email,
                    id = response.id,
                    password = password,
                    firstName = response.firstName,
                    secondName = response.secondName,
                    lastName = response.lastName,
                    group = response.group,
                )
            )
            _authorizedFlow.emit(true)
            return RegisterResult.OK
        } catch (e: Exception) {
            e
            RegisterResult.ERROR
        }
    }

    override suspend fun logout() {
        store.drop()
        _authorizedFlow.emit(false)
    }
}