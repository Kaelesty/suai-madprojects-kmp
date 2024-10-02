package com.kaelesty.madprojects_kmp.blocs.register

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.auth.register.RegisterUseCase
import com.kaelesty.domain.common.UserType
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStore.Intent
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStore.Label
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStore.State
import kotlinx.coroutines.launch

internal interface RegisterStore : Store<Intent, State, Label> {

	sealed interface Intent {
		class SetLogin(val newValue: String): Intent
		class SetPassword(val newValue: String): Intent
		class SetUserType(val newValue: UserType): Intent
		data object Submit: Intent
		data object DropError: Intent
	}

	data class State(
		val login: String = "",
		val password: String = "",
		val errorMessage: String = "",
		val userType: UserType = UserType.STUDENT
	)

	sealed interface Label {
		data object SuccessfulRegister: Label
	}
}

internal class RegisterStoreFactory(
    private val storeFactory: StoreFactory,
	private val registerUseCase: RegisterUseCase,
) {

    fun create(): RegisterStore =
        object : RegisterStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegisterStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(registerUseCase) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

	private sealed interface Msg {
		class SetLogin(val newValue: String): Msg
		class SetPassword(val newValue: String): Msg
		class SetUserType(val newValue: UserType): Msg
		data object DropError: Msg
	}

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl(
		private val registerUseCase: RegisterUseCase,
	) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
			when (intent) {
				Intent.DropError -> {
					dispatch(Msg.DropError)
				}
				is Intent.SetLogin -> dispatch(Msg.SetLogin(intent.newValue))
				is Intent.SetPassword -> dispatch(Msg.SetPassword(intent.newValue))
				Intent.Submit -> {
					scope.launch {
						registerUseCase.invoke(state().login, state().password, state().userType)
					}
					//publish(Label.SuccessfulAuth)
				}

				is Intent.SetUserType -> dispatch(Msg.SetUserType(intent.newValue))
			}
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
				Msg.DropError -> copy(errorMessage = "")
				is Msg.SetLogin -> copy(login = msg.newValue)
				is Msg.SetPassword -> copy(password = msg.newValue)
				is Msg.SetUserType -> copy(userType = msg.newValue)
			}
    }
}
