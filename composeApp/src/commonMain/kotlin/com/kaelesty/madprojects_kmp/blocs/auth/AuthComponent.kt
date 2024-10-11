package com.kaelesty.madprojects_kmp.blocs.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterComponent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.WelcomeComponent

interface AuthComponent {

	val stack: Value<ChildStack<*, Child>>

	sealed interface Child {

		class Login(val component: LoginComponent): Child

		class Welcome(val component: WelcomeComponent): Child

		class Register(val component: RegisterComponent): Child
	}

	interface Navigator {

		fun toProject()
	}
}