package com.kaelesty.madprojects_kmp.di

import com.kaelesty.data.SocketImpl
import com.kaelesty.domain.messenger.Socket
import org.koin.dsl.module

val dataModule = module {
	single<Socket> {
		SocketImpl()
	}
}