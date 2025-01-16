package com.kaelesty.madprojects.data.repos

import com.kaelesty.madprojects.data.repos.auth.AuthRepoImpl
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import org.koin.dsl.module

val reposModule = module {

    single<AuthRepo> {
        AuthRepoImpl(
            apiService = get()
        )
    }
}