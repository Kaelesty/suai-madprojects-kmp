package com.kaelesty.madprojects.domain.stores

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects.domain.repos.auth.LoginResult
import com.kaelesty.madprojects.shared.validateEmail
import com.kaelesty.madprojects.shared.validatePassword
import com.kaelesty.madprojects.domain.stores.LoginStore.Intent
import com.kaelesty.madprojects.domain.stores.LoginStore.Label
import com.kaelesty.madprojects.domain.stores.LoginStore.State
import kotlinx.coroutines.launch

interface LoginStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class SetEmail(val newValue: String): Intent
        class SetPassword(val newValue: String): Intent
        data object Submit: Intent
        data object DropError: Intent
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val errorMessage: String = "",
        val isLoading: Boolean = false,
    )

    sealed interface Label {
        data class SuccessfulAuth(val jwt: String, val userType: UserType): Label
    }
}

class LoginStoreFactory(
    private val storeFactory: StoreFactory,
    private val authRepo: AuthRepo,
) {

    fun create(): LoginStore =
        object : LoginStore, Store<Intent, State, Label> by storeFactory.create(
            name = "LoginStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(authRepo) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        class SetEmail(val newValue: String): Msg
        class SetPassword(val newValue: String): Msg
        data object DropError: Msg
        class SetError(val newValue: String): Msg
        class SetLoading(val newValue: Boolean): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl(
        private val authRepo: AuthRepo,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.DropError -> {
                    dispatch(Msg.DropError)
                }
                is Intent.SetEmail -> dispatch(Msg.SetEmail(intent.newValue.trim()))
                is Intent.SetPassword -> dispatch(Msg.SetPassword(intent.newValue.trim()))
                Intent.Submit -> {

                    if (! validateEmail(state().email)) {
                        dispatch(Msg.SetError("Неверный email"))
                        return
                    }

                    if (! validatePassword(state().password)) {
                        dispatch(Msg.SetError("Пароль должен содержать минимум 8 символов"))
                        return
                    }

                    dispatch(Msg.SetLoading(true))

                    scope.launch {
                        with(state()) {
                            val res = authRepo.login(
                                email = email, password = password
                            )

                            when (res) {
                                LoginResult.OK -> {

                                }
                                LoginResult.ERROR -> {
                                    dispatch(Msg.SetError("Ошибка авторизации"))
                                }

                                LoginResult.BAD_CREDENTIALS -> {
                                    dispatch(Msg.SetError("Проверьте введенные данные"))
                                }
                            }
                            dispatch(Msg.SetLoading(false))
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.DropError -> copy(errorMessage = "")
                is Msg.SetEmail -> copy(email = msg.newValue)
                is Msg.SetPassword -> copy(password = msg.newValue)
                is Msg.SetError -> copy(errorMessage = msg.newValue)
                is Msg.SetLoading -> copy(isLoading = msg.newValue)
            }
    }
}
