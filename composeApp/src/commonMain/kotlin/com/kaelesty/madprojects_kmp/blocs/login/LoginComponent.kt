package com.kaelesty.madprojects_kmp.blocs.login

import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {

	val state: StateFlow<LoginStore.State>

	fun setLogin(newValue: String)

	fun setPassword(newValue: String)

	fun dropError()

	fun submit()

	fun back()

	interface Navigator {

		fun onSuccessfulLogin()

		fun back()
	}
}