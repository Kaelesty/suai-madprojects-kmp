package com.kaelesty.madprojects_kmp

import android.app.Application
import com.kaelesty.domain.di.domainModule
import com.kaelesty.madprojects_kmp.di.dataModule
import com.kaelesty.madprojects_kmp.di.decomposeModule
import com.kaelesty.madprojects_kmp.di.mviModule
import com.kaelesty.madprojects_kmp.ui.lock.lockModule
import org.koin.core.context.GlobalContext.startKoin

class MApp: Application() {

	init {
		startKoin {
			modules(mviModule, decomposeModule, domainModule, lockModule, dataModule)
		}
	}
}