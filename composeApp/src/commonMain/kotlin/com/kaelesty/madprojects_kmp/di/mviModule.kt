package com.kaelesty.madprojects_kmp.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityStoreFactory
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginStoreFactory
import com.kaelesty.madprojects_kmp.blocs.project.ProjectStoreFactory
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterStoreFactory
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStoreFactory
import org.koin.dsl.module

val mviModule = module {

	single<ActivityStoreFactory> {
		ActivityStoreFactory(
			storeFactory = get()
		)
	}

	single<ProjectStoreFactory> {
		ProjectStoreFactory(
			storeFactory = get()
		)
	}

	single<RegisterStoreFactory> {
		RegisterStoreFactory(
			storeFactory = get(),
			registerUseCase = get(),
			lock = get(),
		)
	}

	single<LoginStoreFactory> {
		LoginStoreFactory(
			storeFactory = get(),
			loginUseCase = get(),
			lock = get(),
		)
	}

	single<MessengerStoreFactory> {
		MessengerStoreFactory(
			storeFactory = get(),
			messenger = get()
		)
	}

	single<StoreFactory> {
		DefaultStoreFactory()
	}
}