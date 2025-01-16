package com.kaelesty.madprojects.view.components.root

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects.domain.stores.root.RootStore
import com.kaelesty.madprojects.view.components.auth.AuthContent
import com.kaelesty.madprojects.view.components.main.MainContent

@Composable
fun RootContent(
    component: RootComponent
) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Auth -> AuthContent(instance.component)
            is RootComponent.Child.Main -> MainContent(instance.component)
        }
    }
}