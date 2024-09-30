package com.kaelesty.madprojects_kmp.decompose.auth.login

import com.kaelesty.madprojects_kmp.mvi.LoginStore
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {

	val state: StateFlow<LoginStore.State>

	fun setLogin(newValue: String)

	fun setPassword(newValue: String)

	fun dropError()

	fun submit()
}