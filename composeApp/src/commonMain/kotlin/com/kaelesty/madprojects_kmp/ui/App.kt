package com.kaelesty.madprojects_kmp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.auth.AuthContent
import com.kaelesty.madprojects_kmp.blocs.root.RootComponent
import com.kaelesty.madprojects_kmp.blocs.root.RootContent
import com.kaelesty.madprojects_kmp.ui.lock.Lock
import com.kaelesty.madprojects_kmp.ui.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App(
    rootComponent: RootComponent,
    authorizedFlow: StateFlow<Boolean>
) {

    val authorized by authorizedFlow.collectAsState()

    LaunchedEffect(authorized) {
        rootComponent.setAuthorized(authorized)
    }

    AppTheme(
        isDarkTheme = false
    ) {
        RootContent(
            rootComponent,
        )
    }
}