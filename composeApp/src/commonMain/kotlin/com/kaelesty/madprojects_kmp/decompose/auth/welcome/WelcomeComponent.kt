package com.kaelesty.madprojects_kmp.decompose.auth.welcome

interface WelcomeComponent {

	val shouldPlayAnimation: Boolean

	fun enter()

	fun register()

	interface Navigator {

		fun toEnter()

		fun toRegister()
	}
}