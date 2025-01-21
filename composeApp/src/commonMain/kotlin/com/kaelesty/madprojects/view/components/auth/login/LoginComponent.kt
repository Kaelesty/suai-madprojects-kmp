package com.kaelesty.madprojects_kmp.blocs.auth.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects.domain.stores.LoginStore
import com.kaelesty.madprojects.domain.stores.LoginStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface LoginComponent {

	val state: StateFlow<LoginStore.State>

	fun setLogin(newValue: String)

	fun setPassword(newValue: String)

	fun dropError()

	fun submit()

	fun back()

	interface Factory {

		fun create(c: ComponentContext, n: Navigator): LoginComponent
	}

	interface Navigator {
		fun back()
	}
}

class DefaultLoginComponent(
    private val componentContext: ComponentContext,
    private val navigator: LoginComponent.Navigator,
    private val storeFactory: LoginStoreFactory,
): LoginComponent, ComponentContext by componentContext {

	private val scope = CoroutineScope(Dispatchers.Main)

	private val store: LoginStore
			= instanceKeeper.getStore {
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
			is LoginStore.Label.SuccessfulAuth -> { }
		}
	}

	override fun setLogin(newValue: String) {
		store.accept(LoginStore.Intent.SetEmail(newValue))
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