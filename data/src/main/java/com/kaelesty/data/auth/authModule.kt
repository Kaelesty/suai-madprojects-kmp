package com.kaelesty.data.auth

import com.kaelesty.data.auth.apiService.AuthApiService
import com.kaelesty.data.auth.apiService.AuthApiServiceImpl
import com.kaelesty.domain.auth.AuthenticationManager
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val authModule = module {

    single<AuthApiService> {
        AuthApiServiceImpl(
            client = get()
        )
    }

    single<AuthenticationManager> {
        AuthenticationManagerImpl(
            authApiService = get(),
            store = get()
        )
    }
}