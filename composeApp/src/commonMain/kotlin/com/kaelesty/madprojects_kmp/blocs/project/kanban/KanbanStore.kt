package com.kaelesty.madprojects_kmp.blocs.project.kanban

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.domain.messenger.Socket
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanStore.Intent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanStore.Label
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanStore.State
import entities.KanbanState
import kotlinx.coroutines.launch

interface KanbanStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object Close: Intent

        data class CreateKard(
            val title: String,
            val desc: String,
            val columnId: Int
        ): Intent

        data class CreateColumn(val name: String): Intent

        data class MoveKard(
            val kardId: Int, val columnId: Int,
            val newColumnId: Int, val newOrder: Int,
        ): Intent

        data class MoveColumn(
            val columnId: Int,
            val newOrder: Int,
        ): Intent

        data class UpdateKard(
            val kard: KanbanState.Kard,
            val name: String,
            val desc: String,
        ): Intent

        data class DeleteKard(
            val kardId: Int
        ): Intent

        data class DeleteColumn(
            val columnId: Int
        ): Intent
    }

    data class State(
        val kanbanState: KanbanState?
    )

    sealed interface Label {
    }
}

class KanbanStoreFactory(
    private val storeFactory: StoreFactory,
    private val socket: Socket,
) {

    fun create(): KanbanStore =
        object : KanbanStore, Store<Intent, State, Label> by storeFactory.create(
            name = "KanbanStore",
            initialState = State(null),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class SetState(val newState: KanbanState): Action
    }

    private sealed interface Msg {
        data class SetState(val newState: KanbanState): Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                socket.actionsFlow.collect {
                    if (it is entities.Action.Kanban.SetState) {
                        dispatch(Action.SetState(it.kanban))
                    }
                }
            }
            scope.launch {
                socket.acceptIntent(
                    entities.Intent.Kanban.Start
                )
                socket.acceptIntent(
                    entities.Intent.Kanban.GetKanban
                )
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {

                Intent.Close -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.Stop
                        )
                    }
                }
                is Intent.CreateColumn -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.CreateColumn(
                                name = intent.name,
                            )
                        )
                    }
                }
                is Intent.CreateKard -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.CreateKard(
                                name = intent.title,
                                desc = intent.desc,
                                columnId = intent.columnId
                            )
                        )
                    }
                }
                is Intent.MoveKard -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.MoveKard(
                                id = intent.kardId,
                                columnId = intent.columnId,
                                newPosition = intent.newOrder,
                                newColumnId = intent.newColumnId
                            )
                        )
                    }
                }
                is Intent.MoveColumn -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.MoveColumn(
                                id = intent.columnId,
                                newPosition = intent.newOrder,
                            )
                        )
                    }
                }

                is Intent.UpdateKard -> {
                    scope.launch {
                        with (intent) {
                            socket.acceptIntent(
                                entities.Intent.Kanban.UpdateKard(
                                    id = kard.id,
                                    name = if (name == kard.title) null else name,
                                    desc = if (desc == kard.desc) null else desc,
                                )
                            )
                        }
                    }
                }

                is Intent.DeleteColumn -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.DeleteColumn(intent.columnId)
                        )
                    }
                }
                is Intent.DeleteKard -> {
                    scope.launch {
                        socket.acceptIntent(
                            entities.Intent.Kanban.DeleteKard(intent.kardId)
                        )
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.SetState -> dispatch(Msg.SetState(action.newState))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.SetState -> State(
                    kanbanState = message.newState
                )
            }
    }
}
