package com.kaelesty.madprojects

import android.app.Application
import com.kaelesty.madprojects.data.dataModule
import com.kaelesty.madprojects.domain.domainModule
import com.kaelesty.madprojects.view.viewModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MApp: Application() {

	private val platformModule = module {

	}

	init {
		startKoin {
			modules(dataModule, platformModule, domainModule, viewModule)
		}
	}
}