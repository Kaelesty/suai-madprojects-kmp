package com.kaelesty.madprojects_kmp.decompose.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.decompose.auth.AuthComponent

interface RootComponent {

	val stack: Value<ChildStack<*, Child>>

	sealed interface Child {

		class Auth(val component: AuthComponent): Child
	}
}