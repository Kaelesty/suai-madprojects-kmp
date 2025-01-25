package com.kaelesty.madprojects.data.remote

import com.kaelesty.madprojects.data.features.auth.AuthApiService
import com.kaelesty.madprojects.data.features.curatorship.CuratorshipApiService
import com.kaelesty.madprojects.data.features.github.GithubApiService
import com.kaelesty.madprojects.data.features.profile.ProfileApiService
import com.kaelesty.madprojects.data.features.project.ProjectApiService
import com.kaelesty.madprojects.data.features.sprints.SprintsApiService
import io.ktor.client.HttpClient
import org.koin.dsl.module

val remoteModule = module {

    single<HttpClient> {
        httpClient
    }

    factory<SprintsApiService> {
        SprintsApiService(
            httpClient = get()
        )
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

    factory<GithubApiService> {
        GithubApiService(
            httpClient = get()
        )
    }
}