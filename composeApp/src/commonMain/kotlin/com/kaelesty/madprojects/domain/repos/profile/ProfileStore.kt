package com.kaelesty.madprojects.domain.repos.profile

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.repos.profile.ProfileStore.Intent
import com.kaelesty.madprojects.domain.repos.profile.ProfileStore.Label
import com.kaelesty.madprojects.domain.repos.profile.ProfileStore.State
import kotlinx.coroutines.launch

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    sealed interface State {

        data object Loading: State

        data class CommonProfile(val profile: Profile.Common): State

        data class CuratorProfile(val profile: Profile.Curator): State

        data object Error: State
    }

    sealed interface Label {

    }
}

class ProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val profileRepo: ProfileRepo
) {

    fun create(): ProfileStore =
        object : ProfileStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProfileStore",
            initialState = State.Loading,
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(profileRepo) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object LoadProfile: Action
    }

    private sealed interface Msg {

        data class SetProfile(val profile: Profile): Msg

        data object SetError: Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.LoadProfile)
        }
    }

    private class ExecutorImpl(
        private val profileRepo: ProfileRepo
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadProfile -> {
                    scope.launch {
                        val profile = profileRepo.getProfile()
                        dispatch(
                            profile?.let {
                                Msg.SetProfile(it)
                            } ?: Msg.SetError
                        )
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                Msg.SetError -> State.Error
                is Msg.SetProfile -> when (message.profile) {
                    is Profile.Curator -> State.CuratorProfile(message.profile)
                    is Profile.Common -> State.CommonProfile(message.profile)
                }
            }
    }
}
