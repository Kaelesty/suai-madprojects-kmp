package com.kaelesty.madprojects_kmp.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.kaelesty.madprojects_kmp.mvi.LoginStoreFactory
import org.koin.dsl.module

val mviModule = module {

	single<LoginStoreFactory> {
		LoginStoreFactory(
			storeFactory = get()
		)
	}

	single<StoreFactory> {
		DefaultStoreFactory()
	}
}