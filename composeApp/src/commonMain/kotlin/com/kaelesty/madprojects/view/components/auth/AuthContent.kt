package com.kaelesty.madprojects.view.components.auth

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects.view.components.auth.register.RegisterContent
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginContent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.WelcomeContent

@Composable
fun AuthContent(
    authComponent: AuthComponent
) {
    Children(
        stack = authComponent.stack,
        animation = stackAnimation(slide())
    ) {
        when (val instance = it.instance) {
            is AuthComponent.Child.Welcome -> WelcomeContent(instance.component)
            is AuthComponent.Child.Login -> LoginContent(instance.component)
            is AuthComponent.Child.Register -> RegisterContent(instance.component)
        }
    }
}