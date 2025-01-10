package com.kaelesty.madprojects_kmp.blocs.createProject

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.memberProfile.createProject.Curator
import com.kaelesty.domain.memberProfile.createProject.GetCuratorsListUseCase
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore.Intent
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore.Label
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore.State
import kotlinx.coroutines.launch

interface CreateProjectStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class UpdateName(val new: String) : Intent
        data class UpdateDesc(val new: String) : Intent
        data class UpdateMembersCount(val new: String) : Intent
        data class UpdateCurator(val new: State.Curator?) : Intent
        data class LinkRepo(val link: String) : Intent
        data class UnlinkRepo(val repo: State.Repo) : Intent
    }

    data class State(
        val name: String = "",
        val membersCount: Int = 1,
        val desc: String = "",
        val curators: List<Curator> = listOf(),
        val selectedCurator: Curator? = null,
        val repos: List<Repo> = listOf(),
    ) {
        data class Curator(
            val id: String,
            val name: String
        )

        data class Repo(
            val link: String,
            val name: String
        )
    }

    sealed interface Label {
        data class RepoLinkFinished(val result: Boolean): Label
    }
}

class CreateProjectStoreFactory(
    private val storeFactory: StoreFactory,
    private val getCuratorsListUseCase: GetCuratorsListUseCase,
) {

    fun create(): CreateProjectStore =
        object : CreateProjectStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CreateProjectStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        object UpdateCuratorsList: Action
    }

    private sealed interface Msg {
        data class UpdateName(val new: String) : Msg
        data class UpdateDesc(val new: String) : Msg
        data class UpdateMembersCount(val new: Int) : Msg
        data class UpdateCurator(val new: State.Curator?) : Msg
        data class LinkRepo(val link: String) : Msg
        data class UnlinkRepo(val repo: State.Repo) : Msg
        data class UpdateCuratorsList(val new: List<Curator>): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.UpdateCuratorsList)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.LinkRepo -> dispatch(Msg.LinkRepo(intent.link))
                is Intent.UnlinkRepo -> dispatch(Msg.UnlinkRepo(intent.repo))
                is Intent.UpdateCurator -> dispatch(Msg.UpdateCurator(intent.new))
                is Intent.UpdateDesc -> dispatch(Msg.UpdateDesc(intent.new))
                is Intent.UpdateMembersCount -> dispatch(
                    Msg.UpdateMembersCount(
                        intent.new.toIntOrNull() ?: 1
                    )
                )

                is Intent.UpdateName -> dispatch(Msg.UpdateName(intent.new))
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.UpdateCuratorsList -> {
                    scope.launch {
                        val curators = getCuratorsListUseCase()
                        dispatch(Msg.UpdateCuratorsList(curators))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.LinkRepo -> copy(
                    repos = repos.toMutableList()
                        .apply {
                            add(
                                State.Repo(
                                    link = message.link,
                                    name = message.link.split("/").last()
                                )
                            )
                        }
                        .toList()
                )

                is Msg.UnlinkRepo -> copy(
                    repos = repos.filter {
                        it.link != message.repo.link
                    }
                )
                is Msg.UpdateCurator -> copy(
                    selectedCurator = message.new
                )
                is Msg.UpdateDesc -> copy(
                    desc = message.new
                )
                is Msg.UpdateMembersCount -> copy(
                    membersCount = message.new
                )
                is Msg.UpdateName -> copy(
                    name = message.new
                )

                is Msg.UpdateCuratorsList -> copy(
                    curators = message.new.map {
                        State.Curator(it.id, it.name)
                    }
                )
            }
    }
}
