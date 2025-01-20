package com.kaelesty.madprojects.data.remote

import com.kaelesty.madprojects.data.remote.auth.AuthApiService
import com.kaelesty.madprojects.data.remote.profile.ProfileApiService
import io.ktor.client.HttpClient
import org.koin.dsl.module

val remoteModule = module {

    single<HttpClient> {
        httpClient
    }

    factory<AuthApiService> {
        AuthApiService(
            httpClient = get()
        )
    }

    factory<ProfileApiService> {
        ProfileApiService(
            httpClient = get()
        )
    }
}