package com.kaelesty.madprojects_kmp.di

import com.kaelesty.data.MessengerImpl
import com.kaelesty.domain.messenger.Messenger
import org.koin.dsl.module

val dataModule = module {
	factory<Messenger> {
		MessengerImpl()
	}
}