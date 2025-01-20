package com.kaelesty.madprojects.data.repos

import com.kaelesty.madprojects.data.local.PreferencesStorage
import com.kaelesty.madprojects.data.remote.profile.ProfileApiService
import com.kaelesty.madprojects.data.repos.auth.AuthRepoImpl
import com.kaelesty.madprojects.data.repos.auth.LoginManager
import com.kaelesty.madprojects.data.repos.profile.ProfileRepoImpl
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects.domain.repos.profile.ProfileRepo
import org.koin.dsl.module

val reposModule = module {

    factory<ProfileRepo> {
        ProfileRepoImpl(
            loginManager = get(),
            profileApiService = get()
        )
    }

    factory<AuthRepo> {
        AuthRepoImpl(
            apiService = get(),
            preferencesStorage = get(),
            loginManager = get()
        )
    }

    single<LoginManager> {
        LoginManager()
    }
}