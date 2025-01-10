package com.kaelesty.madprojects_kmp.ui.lock

import org.koin.dsl.module

val lockModule = module {
	single<Lock> {
		LoadingLock()
	}
}