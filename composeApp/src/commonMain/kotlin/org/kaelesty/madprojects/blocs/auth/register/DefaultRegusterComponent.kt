package com.kaelesty.madprojects_kmp.blocs.auth.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.domain.auth.AuthenticationManager
import com.kaelesty.domain.common.UserType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultRegisterComponent(
	private val componentContext: ComponentContext,
	private val storeFactory: RegisterStoreFactory,
	private val navigator: RegisterComponent.Navigator,
	private val authenticationManager: AuthenticationManager,
): RegisterComponent, ComponentContext by componentContext {

	private val scope = CoroutineScope(Dispatchers.IO)

	private val store = instanceKeeper.getStore {
		storeFactory.create(authenticationManager)
			.apply {
				scope.launch {
					labels.collect {
						acceptLabel(it)
					}
				}
			}
	}

	private fun acceptLabel(label: RegisterStore.Label) {
		when (label) {
			RegisterStore.Label.SuccessfulRegister -> {

			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<RegisterStore.State>
		get() = store.stateFlow

	override fun submit() {
		store.accept(RegisterStore.Intent.Submit)
	}

	override fun setLogin(newValue: String) {
		store.accept(RegisterStore.Intent.SetEmail(newValue))
	}

	override fun setPassword(newValue: String) {
		store.accept(RegisterStore.Intent.SetPassword(newValue))
	}

	override fun setRepeatPassword(newValue: String) {
		store.accept(RegisterStore.Intent.SetRepeatPassword(newValue))
	}

	override fun setUserType(newValue: UserType) {
		store.accept(RegisterStore.Intent.SetUserType(newValue))
	}

	override fun dropError() {
		store.accept(RegisterStore.Intent.DropError)
	}

	override fun back() {
		navigator.back()
	}

	override fun setUsername(newValue: String) {
		store.accept(RegisterStore.Intent.SetUsername(newValue))
	}

	override fun setFirstname(newValue: String) {
		store.accept(RegisterStore.Intent.SetFirstName(newValue))
	}

	override fun setSecondname(newValue: String) {
		store.accept(RegisterStore.Intent.SetSecondName(newValue))
	}

	override fun setLastname(newValue: String) {
		store.accept(RegisterStore.Intent.SetLastName(newValue))
	}

	override fun setGroup(newValue: String) {
		store.accept(RegisterStore.Intent.SetGroup(newValue))
	}
}