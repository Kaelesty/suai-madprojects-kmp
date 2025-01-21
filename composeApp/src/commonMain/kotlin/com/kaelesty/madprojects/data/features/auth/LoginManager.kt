package com.kaelesty.madprojects.data.features.auth

import com.kaelesty.data.remote.auth.AuthorizedResponse
import com.kaelesty.madprojects.data.UnauthorizedException
import com.kaelesty.madprojects.data.suspended
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginManager {

    private val _authorization = MutableStateFlow<AuthorizedResponse?>(null)
    val authorization = _authorization.asStateFlow()

    suspend fun authorize(res: AuthorizedResponse) {
        _authorization.emit(res)
    }

    suspend fun unauthorize() {
        _authorization.emit(null)
    }

    suspend fun getTokenOrThrow(): String {
        return _authorization.value?.accessToken ?: throw UnauthorizedException.suspended {
            unauthorize()
        }
    }
}