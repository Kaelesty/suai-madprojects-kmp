package com.kaelesty.madprojects.data.local

import org.koin.dsl.module

val localModule = module {

    single<PreferencesStorage> {
        PreferencesStorage(
            dataStore = get()
        )
    }
}