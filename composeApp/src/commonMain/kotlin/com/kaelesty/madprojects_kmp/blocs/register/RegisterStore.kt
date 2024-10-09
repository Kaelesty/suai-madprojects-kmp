package com.kaelesty.madprojects_kmp.blocs.register

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.auth.register.RegisterUseCase
import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.common.UserType
import com.kaelesty.domain.tools.validateEmail
import com.kaelesty.domain.tools.validatePassword
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStore.Intent
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStore.Label
import com.kaelesty.madprojects_kmp.blocs.register.RegisterStore.State
import com.kaelesty.madprojects_kmp.ui.lock.Lock
import kotlinx.coroutines.launch

interface RegisterStore : Store<Intent, State, Label> {

	sealed interface Intent {
		class SetLogin(val newValue: String): Intent
		class SetPassword(val newValue: String): Intent
		class SetRepeatPassword(val newValue: String): Intent
		class SetUserType(val newValue: UserType): Intent
		class SetGithubLink(val newValue: String): Intent
		class SetUsername(val newValue: String): Intent
		data object Submit: Intent
		data object DropError: Intent
	}

	data class State(
		val username: String = "",
		val login: String = "",
		val password: String = "",
		val repeatPassword: String = "",
		val githubLink: String = "",
		val errorMessage: String = "",
		val userType: UserType = UserType.STUDENT
	)

	sealed interface Label {
		data object SuccessfulRegister: Label
	}
}

class RegisterStoreFactory(
    private val storeFactory: StoreFactory,
	private val registerUseCase: RegisterUseCase,
	private val lock: Lock,
) {

    fun create(): RegisterStore =
        object : RegisterStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegisterStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(registerUseCase, lock) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

	private sealed interface Msg {
		class SetLogin(val newValue: String): Msg
		class SetPassword(val newValue: String): Msg
		class SetRepeatPassword(val newValue: String): Msg
		class SetUserType(val newValue: UserType): Msg
		class SetGithubLink(val newValue: String): Msg
		class SetUsername(val newValue: String): Msg
		class SetError(val newValue: String): Msg
		data object DropError: Msg
	}

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl(
		private val registerUseCase: RegisterUseCase,
		private val lock: Lock
	) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
			when (intent) {
				Intent.DropError -> {
					dispatch(Msg.DropError)
				}
				is Intent.SetLogin -> dispatch(Msg.SetLogin(intent.newValue.trim()))
				is Intent.SetPassword -> dispatch(Msg.SetPassword(intent.newValue.trim()))
				Intent.Submit -> {

					if (! validateEmail(state().login)) {
						dispatch(Msg.SetError("Неверный email"))
						return
					}

					if (! validatePassword(state().password)) {
						dispatch(Msg.SetError("Пароль должен содержать минимум 8 символов"))
						return
					}

					if (state().password != state().repeatPassword) {
						dispatch(Msg.SetError("Пароли не совпадают"))
						return
					}

					scope.launch {
						when (
							val result = lock.proceed {
								registerUseCase.invoke(
									state().login,
									state().password,
									state().userType
								)
							}
						) {
							is UseCaseResult.BadRequest -> {
								dispatch(Msg.SetError(result.error.ui()))
							}
							is UseCaseResult.ExternalError -> {
								dispatch(Msg.SetError("Ошибка сервера. Повторите попытку позже."))
							}
							is UseCaseResult.Success -> {
								publish(Label.SuccessfulRegister)
							}
						}
					}
				}

				is Intent.SetUserType -> dispatch(Msg.SetUserType(intent.newValue))
				is Intent.SetRepeatPassword -> dispatch(Msg.SetRepeatPassword(intent.newValue))
				is Intent.SetGithubLink -> dispatch(Msg.SetGithubLink(intent.newValue))
				is Intent.SetUsername -> dispatch(Msg.SetUsername(intent.newValue))
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
				is Msg.SetRepeatPassword -> copy(repeatPassword = msg.newValue)
				is Msg.SetGithubLink -> copy(githubLink = msg.newValue)
				is Msg.SetUsername -> copy(username = msg.newValue)
				is Msg.SetError -> copy(errorMessage = msg.newValue)
			}
    }
}
