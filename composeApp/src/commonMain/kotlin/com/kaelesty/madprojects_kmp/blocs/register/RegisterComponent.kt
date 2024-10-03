package com.kaelesty.madprojects_kmp.blocs.register

import com.kaelesty.domain.common.UserType
import kotlinx.coroutines.flow.StateFlow

interface RegisterComponent {

	val state: StateFlow<RegisterStore.State>

	fun submit()

	fun setLogin(newValue: String)

	fun setPassword(newValue: String)

	fun setRepeatPassword(newValue: String)

	fun setUserType(newValue: UserType)

	fun setGithubLink(newValue: String)

	fun setUsername(newValue: String)

	fun dropError()

	fun back()

	interface Navigator {

		fun onSuccessfulRegister()

		fun back()
	}
}