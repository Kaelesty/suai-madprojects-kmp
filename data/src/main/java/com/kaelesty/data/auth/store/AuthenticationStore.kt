package com.kaelesty.data.auth.store

import com.kaelesty.domain.auth.AuthenticationManager

interface AuthenticationStore {

    suspend fun get(): AuthenticationManager.AuthenticationContext?

    suspend fun save(new: AuthenticationManager.AuthenticationContext)

    suspend fun updateToken(new: String)

    suspend fun drop()
}