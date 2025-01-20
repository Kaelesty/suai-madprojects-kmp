package com.kaelesty.madprojects.domain.repos.profile

interface ProfileRepo {

    suspend fun getProfile(): Profile?
}