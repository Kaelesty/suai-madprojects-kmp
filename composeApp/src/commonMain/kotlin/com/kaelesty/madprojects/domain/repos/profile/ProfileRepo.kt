package com.kaelesty.madprojects.domain.repos.profile

interface ProfileRepo {

    suspend fun getProfile(): Profile?

    suspend fun getSharedProfile(userId: String): Result<SharedProfileResponse>
}