package com.kaelesty.madprojects_kmp.blocs.project

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.messenger.Socket
import com.kaelesty.madprojects_kmp.blocs.project.ProjectStore.Intent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectStore.Label
import com.kaelesty.madprojects_kmp.blocs.project.ProjectStore.State
import kotlinx.coroutines.launch

interface ProjectStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class Navigate(
            val target: ProjectComponent.Child.NavTarget
        ): Intent
    }

    data class State(
		val projectId: Int,
		val projectName: String? = null,
	)

    sealed interface Label {
        data object NavigateToActivity: Label
        data object NavigateToKanban: Label
        data object NavigateToInfo: Label
        data object NavigateToSettings: Label
        data object NavigateToMessenger: Label
    }
}

class ProjectStoreFactory(
    private val storeFactory: StoreFactory,
    private val socket: Socket
) {

    fun create(
        projectId: Int,
        jwt: String,
    ): ProjectStore =
        object : ProjectStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProjectStore",
            initialState = State(projectId),
            bootstrapper = BootstrapperImpl(jwt, projectId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private inner class BootstrapperImpl(
        private val jwt: String,
        private val projectId: Int
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                socket.connect {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Authorize(
                                jwt = jwt,
                                projectId = projectId
                            )
                        )
                    }
                }
            }

        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent) {
                is Intent.Navigate -> {
                    publish(
                        when(intent.target) {
                            ProjectComponent.Child.NavTarget.Activity -> Label.NavigateToActivity
                            ProjectComponent.Child.NavTarget.Kanban -> Label.NavigateToKanban
                            ProjectComponent.Child.NavTarget.Info -> Label.NavigateToInfo
                            ProjectComponent.Child.NavTarget.Settings -> Label.NavigateToSettings
                            ProjectComponent.Child.NavTarget.Messenger -> Label.NavigateToMessenger
                        }
                    )
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            this
    }
}
