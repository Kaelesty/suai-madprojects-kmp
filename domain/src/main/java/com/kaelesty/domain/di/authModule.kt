package com.kaelesty.domain.di

import com.kaelesty.domain.auth.login.LoginUseCase
import com.kaelesty.domain.auth.register.RegisterUseCase
import org.koin.dsl.module

val authModule = module {

	factory<LoginUseCase> {
		LoginUseCase(
			repo = get()
		)
	}

	factory<RegisterUseCase> {
		RegisterUseCase(
			repo = get()
		)
	}
}