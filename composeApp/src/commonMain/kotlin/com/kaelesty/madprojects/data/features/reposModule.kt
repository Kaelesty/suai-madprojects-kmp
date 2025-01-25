package com.kaelesty.madprojects.data.features

import com.kaelesty.madprojects.data.features.auth.AuthRepoImpl
import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.data.features.curatorship.CuratorshipRepoImpl
import com.kaelesty.madprojects.data.features.github.GithubRepoImpl
import com.kaelesty.madprojects.data.features.profile.ProfileRepoImpl
import com.kaelesty.madprojects.data.features.project.ProjectRepoImpl
import com.kaelesty.madprojects.data.features.sprints.SprintsRepoImpl
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects.domain.repos.curatorship.CuratorshipRepo
import com.kaelesty.madprojects.domain.repos.github.GithubRepo
import com.kaelesty.madprojects.domain.repos.profile.ProfileRepo
import com.kaelesty.madprojects.domain.repos.project.ProjectRepo
import com.kaelesty.madprojects.domain.repos.sprints.SprintsRepo
import io.ktor.client.HttpClient
import org.koin.dsl.module

val reposModule = module {

    factory<ProjectRepo> {
        ProjectRepoImpl(
            projectApiService = get(),
            loginManager = get(),
        )
    }

    factory<SprintsRepo> {
        SprintsRepoImpl(
            apiService = get(),
            loginManager = get(),
        )
    }

    factory<GithubRepo> {
        GithubRepoImpl(
            apiService = get(),
            loginManager = get()
        )
    }

    factory<ProfileRepo> {
        ProfileRepoImpl(
            loginManager = get(),
            profileApiService = get()
        )
    }

    single<AuthRepo> {
        AuthRepoImpl(
            apiService = get(),
            preferencesStorage = get(),
            loginManager = get()
        )
    }

    single<LoginManager> {
        LoginManager(
            authApiService = get(),
            preferencesStorage = get(),
        ).apply {
            val client = get<HttpClient>()
            installInterceptor(client)
        }
    }

    factory<CuratorshipRepo> {
        CuratorshipRepoImpl(
            curatorshipApiService = get(),
            loginManager = get(),
        )
    }
}