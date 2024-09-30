package com.kaelesty.madprojects_kmp.decompose.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.decompose.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.decompose.auth.welcome.WelcomeComponent

interface AuthComponent {

	val stack: Value<ChildStack<*, Child>>

	sealed interface Child {

		class Login(val component: LoginComponent): Child

		class Welcome(val component: WelcomeComponent): Child
	}
}