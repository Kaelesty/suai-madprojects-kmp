package com.kaelesty.madprojects_kmp.blocs.profile

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.memberProfile.getProfile.GetMemberProfileUseCase
import com.kaelesty.domain.memberProfile.getProfile.GetProfileBody
import kotlinx.coroutines.launch

interface ProfileStore : Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> {

    sealed interface Intent {
    }

    data class State(
        val userName: String = "",
        val avatarUrl: String = "https://samsunggamer.hu/uploads/page_texts/sq/0000/032/Among-us-party-32-1605633457.png",
        val group: String = "",
        val email: String = "",
        val projects: List<Project> = listOf(),
        val firstName: String = "",
        val secondName: String = "",
        val lastName: String = "",
    ) {
        data class Project(
            val id: Int,
            val name: String,
        )
    }

    sealed interface Label {
    }
}

class ProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val getMemberProfileUseCase: GetMemberProfileUseCase,
) {

    fun create(): ProfileStore =
        object : ProfileStore, Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> by storeFactory.create(
            name = "ProfileStore",
            initialState = ProfileStore.State(),
            bootstrapper = BootstrapperImpl(getMemberProfileUseCase),
            executorFactory = ProfileStoreFactory::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class SetProfile(val body: GetProfileBody): Action
    }

    private sealed interface Msg {
        data class SetStateBy(val body: GetProfileBody): Msg
    }

    private class BootstrapperImpl(
        private val getMemberProfileUseCase: GetMemberProfileUseCase,
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.SetProfile(getMemberProfileUseCase()))
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<ProfileStore.Intent, Action, ProfileStore.State, Msg, ProfileStore.Label>() {
        override fun executeIntent(intent: ProfileStore.Intent) {

        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.SetProfile -> dispatch(Msg.SetStateBy(action.body))

            }
        }
    }

    private object ReducerImpl : Reducer<ProfileStore.State, Msg> {
        override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State =
            when (msg) {
                is Msg.SetStateBy -> ProfileStore.State(
                    userName = msg.body.userName,
                    avatarUrl = msg.body.avatarUrl,
                    projects = msg.body.projects.map {
                        ProfileStore.State.Project(it.id, it.name)
                    },
                    group = msg.body.group,
                    email = msg.body.email,
                    firstName = msg.body.firstName,
                    secondName = msg.body.secondName,
                    lastName = msg.body.lastName
                )
            }
    }
}
