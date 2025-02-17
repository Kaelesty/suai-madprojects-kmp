package com.kaelesty.madprojects.data.features.profile

import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.repos.profile.Profile
import com.kaelesty.madprojects.domain.repos.profile.ProfileRepo
import com.kaelesty.madprojects.domain.repos.profile.SharedProfileResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class ProfileRepoImpl(
    private val profileApiService: ProfileApiService,
    private val loginManager: LoginManager
): ProfileRepo {

    override suspend fun getSharedProfile(userId: String): Result<SharedProfileResponse> {
        return kotlin.runCatching {
            profileApiService.getSharedProfile(
            token = loginManager.getTokenOrThrow(),
            userId = userId
        ).getOrThrow().body<SharedProfileResponse>()
        }
    }

    override suspend fun getProfile(): Profile? {
        return loginManager.authorization.value?.let { authorized ->
            val response = when (authorized.userType) {
                UserType.Common -> {
                    profileApiService.getCommonProfile(authorized.accessToken)
                }
                UserType.Curator -> {
                    profileApiService.getCuratorProfile(authorized.accessToken)
                }
            }

            return response?.let {
                when (it.status) {
                    HttpStatusCode.OK -> {
                        return when (authorized.userType) {
                            UserType.Common -> {
                                it.body<Profile.Common>()
                            }
                            UserType.Curator -> {
                                it.body<Profile.Curator>()
                            }
                        }
                    }
                    HttpStatusCode.Unauthorized -> {
                        loginManager.unauthorize()
                        null
                    }
                    else -> {
                        null
                    }
                }
            }
        }
    }
}