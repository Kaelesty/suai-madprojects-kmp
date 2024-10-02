package com.kaelesty.madprojects_kmp.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.kaelesty.madprojects_kmp.blocs.login.LoginStoreFactory
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStoreFactory
import org.koin.dsl.module

val mviModule = module {

	single<RegisterStoreFactory> {
		RegisterStoreFactory(
			storeFactory = get(),
			registerUseCase = get(),
		)
	}

	single<LoginStoreFactory> {
		LoginStoreFactory(
			storeFactory = get(),
			loginUseCase = get(),
		)
	}

	single<StoreFactory> {
		DefaultStoreFactory()
	}
}