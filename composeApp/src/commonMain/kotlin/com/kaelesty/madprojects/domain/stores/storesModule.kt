package com.kaelesty.madprojects.domain.stores

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterStoreFactory
import org.koin.dsl.module

val storesModule = module {

    single<StoreFactory> {
        DefaultStoreFactory()
    }

    single<RootStoreFactory> {
        RootStoreFactory(
            storeFactory = get(),
            authRepo = get(),
        )
    }

    factory<RegisterStoreFactory> {
        RegisterStoreFactory(
            storeFactory = get(),
            authRepo = get(),
        )
    }

    factory<LoginStoreFactory> {
        LoginStoreFactory(
            storeFactory = get(),
            authRepo = get(),
        )
    }

    factory<ProfileStoreFactory> {
        ProfileStoreFactory(
            storeFactory = get(),
            profileRepo = get(),
        )
    }

    factory<ProjectCreationStoreFactory> {
        ProjectCreationStoreFactory(
            storeFactory = get(),
            projectRepo = get(),
        )
    }
}