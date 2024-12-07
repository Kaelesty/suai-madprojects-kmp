package com.kaelesty.madprojects_kmp.blocs.memberProfile.profile

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
        val avatarUrl: String = "",
        val group: String = "",
        val githubLink: String = "",
        val email: String = "",
        val projects: List<Project> = listOf(),
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

    fun create(jwt: String): ProfileStore =
        object : ProfileStore, Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> by storeFactory.create(
            name = "ProfileStore",
            initialState = ProfileStore.State(),
            bootstrapper = BootstrapperImpl(getMemberProfileUseCase, jwt),
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
        private val jwt: String
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                when (val res = getMemberProfileUseCase.invoke(
                    jwt = jwt
                )) {
                    is UseCaseResult.BadRequest -> TODO()
                    is UseCaseResult.ExternalError -> TODO()
                    is UseCaseResult.Success -> dispatch(
                        Action.SetProfile(res.body)
                    )
                }
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
                    githubLink = msg.body.githubLink,
                    avatarUrl = msg.body.avatarUrl,
                    projects = msg.body.projects.map {
                        ProfileStore.State.Project(it.id, it.name)
                    },
                    group = msg.body.group,
                    email = msg.body.email,
                )
            }
    }
}
