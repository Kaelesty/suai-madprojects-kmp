package com.kaelesty.madprojects_kmp.blocs.auth.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultLoginComponent(
	private val componentContext: ComponentContext,
	private val storeFactory: LoginStoreFactory,
	private val navigator: LoginComponent.Navigator
): LoginComponent, ComponentContext  by componentContext {

	private val scope = CoroutineScope(Dispatchers.IO)

	private val store = instanceKeeper.getStore {
		storeFactory.create()
	}.apply {
		scope.launch(Dispatchers.Main) {
			labels.collect {
				acceptLabel(it)
			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<LoginStore.State>
		get() = store.stateFlow

	private fun acceptLabel(label: LoginStore.Label) {
		when (label) {
			is LoginStore.Label.SuccessfulAuth -> { navigator.onSuccessfulLogin(label.jwt, label.userType) }
		}
	}

	override fun setLogin(newValue: String) {
		store.accept(LoginStore.Intent.SetLogin(newValue))
	}

	override fun setPassword(newValue: String) {
		store.accept(LoginStore.Intent.SetPassword(newValue))
	}

	override fun dropError() {
		store.accept(LoginStore.Intent.DropError)
	}

	override fun submit() {
		store.accept(LoginStore.Intent.Submit)
	}

	override fun back() {
		navigator.back()
	}
}