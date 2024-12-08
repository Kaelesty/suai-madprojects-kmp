package com.kaelesty.madprojects_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.kaelesty.domain.auth.AuthenticationManager
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.root.RootComponent
import com.kaelesty.madprojects_kmp.ui.App
import com.kaelesty.madprojects_kmp.ui.lock.Lock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val rootComponent : RootComponent by inject(clazz = RootComponent::class.java) {
        parametersOf(defaultComponentContext())
    }

    private val authenticationManager: AuthenticationManager
        by inject(clazz = AuthenticationManager::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            authenticationManager.init()
        }
        setContent {
            App(
                rootComponent = rootComponent,
                authorizedFlow = authenticationManager.authorizedFlow,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}