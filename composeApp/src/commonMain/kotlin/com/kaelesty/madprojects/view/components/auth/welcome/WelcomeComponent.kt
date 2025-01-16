package com.kaelesty.madprojects_kmp.blocs.auth.welcome

import com.arkivanov.decompose.ComponentContext

interface WelcomeComponent {

	val shouldPlayAnimation: Boolean

	fun enter()

	fun register()

	interface Factory {

		fun create(c: ComponentContext, n: Navigator): WelcomeComponent
	}

	interface Navigator {

		fun toEnter()

		fun toRegister()
	}
}

class DefaultWelcomeComponent(
	private val componentContext: ComponentContext,
	private val navigator: WelcomeComponent.Navigator,
): ComponentContext by componentContext, WelcomeComponent {

	private var _shouldPlayAnimation: Boolean = true
	override val shouldPlayAnimation: Boolean
		get() = if (_shouldPlayAnimation) {
			_shouldPlayAnimation = false
			true
		} else {
			false
		}

	override fun enter() {
		navigator.toEnter()
	}

	override fun register() {
		navigator.toRegister()
	}
}