package com.kaelesty.madprojects_kmp.blocs.auth.register

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects.domain.repos.auth.RegisterResult
import com.kaelesty.madprojects.shared.validateEmail
import com.kaelesty.madprojects.shared.validatePassword
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterStore.Intent
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterStore.Label
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterStore.State
import kotlinx.coroutines.launch

interface RegisterStore : Store<Intent, State, Label> {

    sealed interface Intent {
        class SetEmail(val newValue: String): Intent
        class SetFirstName(val newValue: String): Intent
        class SetSecondName(val newValue: String): Intent
        class SetLastName(val newValue: String): Intent
        class SetPassword(val newValue: String): Intent
        class SetRepeatPassword(val newValue: String): Intent
        class SetUserType(val newValue: UserType): Intent
        class SetUsername(val newValue: String): Intent
        class SetData(val newValue: String): Intent
        data object Submit: Intent
        data object DropError: Intent
    }

    data class State(
        val username: String = "",
        val firstName: String = "",
        val secondName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val repeatPassword: String = "",
        val errorMessage: String = "",
        val data: String = "",
        val userType: UserType = UserType.Common,
        val isLoading: Boolean = false
    )

    sealed interface Label {
        data object SuccessfulRegister: Label
    }
}

class RegisterStoreFactory(
    private val storeFactory: StoreFactory,
    private val authRepo: AuthRepo,
) {

    fun create(): RegisterStore =
        object : RegisterStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegisterStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(authRepo) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        class SetFirstName(val newValue: String): Msg
        class SetSecondName(val newValue: String): Msg
        class SetLastName(val newValue: String): Msg
        class SetEmail(val newValue: String): Msg
        class SetPassword(val newValue: String): Msg
        class SetRepeatPassword(val newValue: String): Msg
        class SetUserType(val newValue: UserType): Msg
        class SetUsername(val newValue: String): Msg
        class SetData(val newValue: String): Msg
        class SetError(val newValue: String): Msg
        class SetLoading(val newValue: Boolean): Msg
        data object DropError: Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl(
        private val authRepo: AuthRepo,
    ): CoroutineExecutor<Intent, Action, State, Msg, Label>() {
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

                    if (state().password != state().repeatPassword) {
                        dispatch(Msg.SetError("Пароли не совпадают"))
                        return
                    }

                    dispatch(Msg.SetLoading(true))

                    scope.launch {
                        with(state()) {
                            val result = authRepo.register(
                                username = username,
                                firstName = firstName,
                                secondName = secondName,
                                lastName = lastName,
                                data = data,
                                email = email,
                                password = password,
                                userType = userType
                            )
                            when (result) {
                                RegisterResult.OK -> {
                                    // authentication will be handled by higher-level component
                                    // additional navigation isn't required
                                }
                                RegisterResult.BAD_REQUEST -> {
                                    dispatch(Msg.SetError("Ошибка при регистрации"))
                                }

                                RegisterResult.BAD_PASSWORD -> dispatch(Msg.SetError("Слишком слабый пароль"))
                                RegisterResult.BAD_EMAIL -> dispatch(Msg.SetError("Такой email уже зарегистрирован"))
                                RegisterResult.BAD_USERNAME -> dispatch(Msg.SetError("Пользователь с таким именем уже зарегистрирован"))
                                RegisterResult.INTERNET_ERROR -> dispatch(Msg.SetError("Ошибка при регистрации"))
                            }
                            dispatch(Msg.SetLoading(false))
                        }
                    }
                }

                is Intent.SetUserType -> {
                    dispatch(Msg.SetUserType(intent.newValue))
                }
                is Intent.SetRepeatPassword -> dispatch(Msg.SetRepeatPassword(intent.newValue.trim()))
                is Intent.SetUsername -> dispatch(Msg.SetUsername(intent.newValue.trim()))
                is Intent.SetFirstName -> dispatch(Msg.SetFirstName(intent.newValue.trim()))
                is Intent.SetLastName -> dispatch(Msg.SetLastName(intent.newValue.trim()))
                is Intent.SetSecondName -> dispatch(Msg.SetSecondName(intent.newValue.trim()))
                is Intent.SetData -> dispatch(Msg.SetData(intent.newValue.trim()))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.DropError -> copy(errorMessage = "")
                is Msg.SetEmail -> copy(email = msg.newValue)
                is Msg.SetPassword -> copy(password = msg.newValue)
                is Msg.SetUserType -> copy(userType = msg.newValue, data = "")
                is Msg.SetRepeatPassword -> copy(repeatPassword = msg.newValue)
                is Msg.SetUsername -> copy(username = msg.newValue)
                is Msg.SetError -> copy(errorMessage = msg.newValue)
                is Msg.SetFirstName -> copy(firstName = msg.newValue)
                is Msg.SetLastName -> copy(lastName = msg.newValue)
                is Msg.SetSecondName -> copy(secondName = msg.newValue)
                is Msg.SetData -> copy(data = msg.newValue)
                is Msg.SetLoading -> copy(isLoading = msg.newValue)
            }
    }
}
