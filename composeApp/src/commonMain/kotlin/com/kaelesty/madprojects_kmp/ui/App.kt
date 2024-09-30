package com.kaelesty.madprojects_kmp.ui

import androidx.compose.runtime.Composable
import com.kaelesty.madprojects_kmp.decompose.root.RootComponent
import com.kaelesty.madprojects_kmp.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(rootComponent: RootComponent) {

    AppTheme(isDarkTheme = false) {
        RootContent(rootComponent)
    }
}