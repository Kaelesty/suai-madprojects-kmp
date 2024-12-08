package com.kaelesty.madprojects_kmp.blocs.createProject

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore.Intent
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore.Label
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore.State

interface CreateProjectStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class UpdateName(val new: String): Intent
        data class UpdateDesc(val new: String): Intent
        data class UpdateMembersCount(val new: Int): Intent
        data class UpdateCurator(val new: State.Curator?): Intent
        data class LinkRepo(val link: String): Intent
        data class UnlinkRepo(val repo: State.Repo): Intent
    }

    data class State(
        val name: String = "",
        val membersCount: Int = 0,
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
    }
}

class CreateProjectStoreFactory(
    private val storeFactory: StoreFactory
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
    }

    private sealed interface Msg {
        data class UpdateName(val new: String): Msg
        data class UpdateDesc(val new: String): Msg
        data class UpdateMembersCount(val new: Int): Msg
        data class UpdateCurator(val new: State.Curator?): Msg
        data class LinkRepo(val link: String): Msg
        data class UnlinkRepo(val repo: State.Repo): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.LinkRepo -> TODO()
                is Msg.UnlinkRepo -> TODO()
                is Msg.UpdateCurator -> TODO()
                is Msg.UpdateDesc -> TODO()
                is Msg.UpdateMembersCount -> TODO()
                is Msg.UpdateName -> TODO()
            }
    }
}
