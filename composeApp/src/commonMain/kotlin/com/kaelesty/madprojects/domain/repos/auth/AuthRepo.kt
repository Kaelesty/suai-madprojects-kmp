package com.kaelesty.madprojects.domain.repos.auth

import com.kaelesty.madprojects.domain.stores.root.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepo {

    suspend fun login(email: String, password: String): LoginResult

    fun getUser(): StateFlow<User?>
}