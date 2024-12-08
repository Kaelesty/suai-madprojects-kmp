package com.kaelesty.madprojects_kmp

import android.app.Application
import com.kaelesty.data.auth.store.AuthenticationStore
import com.kaelesty.data.dataModule
import com.kaelesty.domain.di.domainModule
import com.kaelesty.madprojects_kmp.di.decomposeModule
import com.kaelesty.madprojects_kmp.di.mviModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MApp: Application() {

	private val platformModule = module {
		single<AuthenticationStore> {
			AndroidAuthenticationStore(this@MApp)
		}
	}

	init {
		startKoin {
			modules(mviModule, decomposeModule, domainModule, dataModule, platformModule)
		}
	}
}