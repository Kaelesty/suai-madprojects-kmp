package com.kaelesty.domain.di

import com.kaelesty.domain.auth.login.LoginUseCase
import org.koin.dsl.module

val authModule = module {

	factory<LoginUseCase> {
		LoginUseCase(
			repo = get()
		)
	}
}