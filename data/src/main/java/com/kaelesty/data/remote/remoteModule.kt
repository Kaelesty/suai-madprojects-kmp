package com.kaelesty.data.remote

import com.kaelesty.data.remote.auth.AuthApiService
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
}