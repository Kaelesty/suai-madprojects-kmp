package com.kaelesty.data.local

import com.kaelesty.domain.auth.UserType

interface AuthPreferencesStore {

    suspend fun save(
        refresh: String,
        refreshExpires: Long,
        access: String,
        accessExpires: Long,
        userType: UserType,
    )

    suspend fun drop()

    suspend fun getAuthorization(): Authorization?

    suspend fun getRefresh(): Refresh?

    data class Authorization(
        val jwt: String,
        val expiresAt: Long,
        val userType: UserType,
    )

    data class Refresh(
        val jwt: String,
        val expiresAt: Long
    )
}