package com.kaelesty.madprojects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.kaelesty.madprojects.view.components.root.RootComponent
import com.kaelesty.madprojects.view.ui.App
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
        }
        setContent {
            App(
                rootComponent = rootComponent,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}