package com.kaelesty.madprojects_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kaelesty.madprojects_kmp.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MadProjects",
    ) {
        App()
    }
}