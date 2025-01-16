package com.kaelesty.madprojects.view.ui

import androidx.compose.runtime.Composable
import com.kaelesty.madprojects.view.components.root.RootComponent
import com.kaelesty.madprojects.view.components.root.RootContent
import com.kaelesty.madprojects_kmp.ui.theme.AppTheme

@Composable
fun App(
    rootComponent: RootComponent,
) {

    AppTheme(
        isDarkTheme = false
    ) {
        RootContent(
            rootComponent,
        )
    }
}