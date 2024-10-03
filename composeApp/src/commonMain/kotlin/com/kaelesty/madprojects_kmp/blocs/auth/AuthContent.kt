package com.kaelesty.madprojects_kmp.blocs.auth

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects_kmp.blocs.login.LoginContent
import com.kaelesty.madprojects_kmp.blocs.register.RegisterComponent
import com.kaelesty.madprojects_kmp.blocs.register.RegisterContent
import com.kaelesty.madprojects_kmp.blocs.welcome.WelcomeContent

@Composable
fun AuthContent(
	component: AuthComponent
) {
	Children(
		stack = component.stack,
		animation = stackAnimation(slide())
	) {
		when (val instance = it.instance) {
			is AuthComponent.Child.Login -> LoginContent(component = instance.component)
			is AuthComponent.Child.Welcome -> WelcomeContent(component = instance.component)
			is AuthComponent.Child.Register -> RegisterContent(component = instance.component)
		}
	}
}