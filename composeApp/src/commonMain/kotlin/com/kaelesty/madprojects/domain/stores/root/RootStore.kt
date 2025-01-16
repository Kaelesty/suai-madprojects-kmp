package com.kaelesty.madprojects.domain.stores.root

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import kotlinx.coroutines.launch

interface RootStore : Store<RootStore.Intent, RootStore.RootState, RootStore.Label> {

    sealed interface Intent {
    }

    sealed interface RootState {

        data object Unauthorized: RootState

        data class Authorized(val user: User): RootState
    }

    sealed interface Label {
    }
}

class RootStoreFactory(
    private val storeFactory: StoreFactory,
    private val authRepo: AuthRepo
) {

    fun create(): RootStore =
        object : RootStore, Store<RootStore.Intent, RootStore.RootState, RootStore.Label> by storeFactory.create(
            name = "RootStore",
            initialState = RootStore.RootState.Unauthorized,
            bootstrapper = BootstrapperImpl(authRepo),
            executorFactory = RootStoreFactory::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object Unauthorized: Action

        data class Authorized(val user: User): Action
    }

    private sealed interface Msg {

        data object Unauthorized: Msg

        data class Authorized(val user: User): Msg
    }

    private class BootstrapperImpl(
        private val authRepo: AuthRepo,
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                authRepo.getUser().collect {
                    if (it == null) {
                        dispatch(Action.Unauthorized)
                    }
                    else {
                        dispatch(Action.Authorized(it))
                    }
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<RootStore.Intent, Action, RootStore.RootState, Msg, RootStore.Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.Authorized -> dispatch(Msg.Authorized(action.user))
                Action.Unauthorized -> dispatch(Msg.Unauthorized)
            }
        }
    }

    private object ReducerImpl : Reducer<RootStore.RootState, Msg> {
        override fun RootStore.RootState.reduce(message: Msg): RootStore.RootState =
            when (message) {
                is Msg.Authorized -> RootStore.RootState.Authorized(message.user)
                Msg.Unauthorized -> RootStore.RootState.Unauthorized
            }
    }
}
