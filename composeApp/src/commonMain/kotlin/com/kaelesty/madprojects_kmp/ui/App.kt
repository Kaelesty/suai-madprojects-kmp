package com.kaelesty.madprojects_kmp.ui

import androidx.compose.runtime.Composable
import com.kaelesty.madprojects_kmp.blocs.root.RootComponent
import com.kaelesty.madprojects_kmp.blocs.root.RootContent
import com.kaelesty.madprojects_kmp.ui.lock.Lock
import com.kaelesty.madprojects_kmp.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App(
    rootComponent: RootComponent,
    lock: Lock
) {

    AppTheme(isDarkTheme = false) {
        RootContent(
            rootComponent,
            lock = lock
        )
    }
}