package com.kaelesty.madprojects_kmp

import android.app.Application
import com.kaelesty.data.dataModule
import com.kaelesty.madprojects_kmp.di.decomposeModule
import com.kaelesty.madprojects_kmp.di.mviModule
import org.koin.core.context.GlobalContext.startKoin

class MApp: Application() {

	init {
		startKoin {
			modules(mviModule, decomposeModule, dataModule)
		}
	}
}