package com.kaelesty.madprojects.data.remote

import com.kaelesty.madprojects.data.features.auth.AuthApiService
import com.kaelesty.madprojects.data.features.curatorship.CuratorshipApiService
import com.kaelesty.madprojects.data.features.profile.ProfileApiService
import com.kaelesty.madprojects.data.features.project.ProjectApiService
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

    factory<ProjectApiService> {
        ProjectApiService(httpClient = get())
    }

    factory<CuratorshipApiService> {
        CuratorshipApiService(
            httpClient = get()
        )
    }
}