package com.kaelesty.madprojects.domain.repos.auth

import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.stores.root.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepo {

    suspend fun login(email: String, password: String): LoginResult

    suspend fun register(
        username: String,
        lastName: String,
        firstName: String,
        secondName: String,
        data: String, // group for common, grade for curator
        email: String,
        password: String,
        userType: UserType,
    ): RegisterResult

    fun getUser(): StateFlow<User?>
}