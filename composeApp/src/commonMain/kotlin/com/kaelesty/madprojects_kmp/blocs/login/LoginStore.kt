package com.kaelesty.madprojects_kmp.blocs.login

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.auth.login.LoginUseCase
import com.kaelesty.madprojects_kmp.blocs.login.LoginStore.Intent
import com.kaelesty.madprojects_kmp.blocs.login.LoginStore.Label
import com.kaelesty.madprojects_kmp.blocs.login.LoginStore.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface LoginStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class SetLogin(val newValue: String): Intent
        class SetPassword(val newValue: String): Intent
        data object Submit: Intent
        data object DropError: Intent
    }

    data class State(
        val login: String = "",
        val password: String = "",
        val errorMessage: String = ""
    )

    sealed interface Label {
        data object SuccessfulAuth: Label
    }
}

class LoginStoreFactory(
    private val storeFactory: StoreFactory,
    private val loginUseCase: LoginUseCase,
) {

    fun create(): LoginStore =
        object : LoginStore, Store<Intent, State, Label> by storeFactory.create(
            name = "LoginStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(loginUseCase) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        class SetLogin(val newValue: String): Msg
        class SetPassword(val newValue: String): Msg
        data object DropError: Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl(
        private val loginUseCase: LoginUseCase
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.DropError -> {
                    dispatch(Msg.DropError)
                }
                is Intent.SetLogin -> dispatch(Msg.SetLogin(intent.newValue))
                is Intent.SetPassword -> dispatch(Msg.SetPassword(intent.newValue))
                Intent.Submit -> {
                    scope.launch { loginUseCase.invoke(state().login, state().password) }
                    //publish(Label.SuccessfulAuth)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.DropError -> copy(errorMessage = "")
                is Msg.SetLogin -> copy(login = msg.newValue)
                is Msg.SetPassword -> copy(password = msg.newValue)
            }
    }
}
