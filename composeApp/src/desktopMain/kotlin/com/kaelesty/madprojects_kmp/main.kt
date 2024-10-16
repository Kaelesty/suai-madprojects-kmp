package com.kaelesty.madprojects_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.kaelesty.domain.di.domainModule
import com.kaelesty.madprojects_kmp.blocs.root.RootComponent
import com.kaelesty.madprojects_kmp.di.dataModule
import com.kaelesty.madprojects_kmp.di.decomposeModule
import com.kaelesty.madprojects_kmp.di.mviModule
import com.kaelesty.madprojects_kmp.ui.App
import com.kaelesty.madprojects_kmp.ui.lock.Lock
import com.kaelesty.madprojects_kmp.ui.lock.lockModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

private const val SAVED_STATE_FILE_NAME = "saved_state.dat"

fun main() = application {

    startKoin {
        modules(mviModule, decomposeModule, domainModule, lockModule, dataModule)
    }

    val lifecycle = LifecycleRegistry()
    //val stateKeeper = StateKeeperDispatcher(File(SAVED_STATE_FILE_NAME).readSerializableContainer())

    val rootComponent : RootComponent by inject(clazz = RootComponent::class.java) { parametersOf(
        DefaultComponentContext(
            lifecycle = lifecycle
        )
    ) }

    val lock: Lock by inject(clazz = Lock::class.java)

    Window(
        onCloseRequest = ::exitApplication,
        title = "MadProjects",
    ) {
        App(
            rootComponent = rootComponent,
            lock = lock,
        )
    }
}