package com.kaelesty.madprojects_kmp.blocs.auth

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.kaelesty.madprojects_kmp.blocs.login.LoginContent
import com.kaelesty.madprojects_kmp.blocs.welcome.WelcomeContent

@Composable
fun AuthContent(
	component: AuthComponent
) {
	Children(
		stack = component.stack
	) {
		when (val instance = it.instance) {
			is AuthComponent.Child.Login -> LoginContent(component = instance.component)
			is AuthComponent.Child.Welcome -> WelcomeContent(component = instance.component)
		}
	}
}