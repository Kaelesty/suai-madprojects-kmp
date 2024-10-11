package com.kaelesty.madprojects_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.kaelesty.madprojects_kmp.blocs.root.RootComponent
import com.kaelesty.madprojects_kmp.ui.App
import com.kaelesty.madprojects_kmp.ui.lock.Lock
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    private val rootComponent : RootComponent by inject(clazz = RootComponent::class.java) { parametersOf(
        defaultComponentContext(), {}
    ) }

    private val lock: Lock by inject(clazz = Lock::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                rootComponent,
                lock = lock
            )
        }
    }
}