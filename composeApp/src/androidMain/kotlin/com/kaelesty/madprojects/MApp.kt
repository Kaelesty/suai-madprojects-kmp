package com.kaelesty.madprojects

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kaelesty.madprojects.data.dataModule
import com.kaelesty.madprojects.data.local.createDataStore
import com.kaelesty.madprojects.domain.domainModule
import com.kaelesty.madprojects.view.viewModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MApp: Application() {

	private val platformModule = module {
		single<DataStore<Preferences>> {
			createDataStore(this@MApp)
		}
	}

	init {
		startKoin {
			modules(dataModule, platformModule, domainModule, viewModule)
		}
	}
}