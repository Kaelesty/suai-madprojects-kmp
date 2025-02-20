package com.kaelesty.madprojects.view.components.main.project.sprint

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.socket.KanbanState
import com.kaelesty.madprojects.domain.repos.sprints.SprintMeta
import com.kaelesty.madprojects.domain.repos.sprints.SprintsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface SprintComponent {

    sealed interface State {

        data class Main(
            val sprintMeta: SprintMeta,
            val kanbanState: KanbanState,
        ): State

        data object Loading: State

        data object Error: State
    }

    interface Navigator {

        fun toKanban()
    }

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,sprintId: String
        ): SprintComponent
    }

    val state: StateFlow<State>

    fun toKanban()
}

class DefaultSprintComponent(
    private val componentContext: ComponentContext,
    private val navigator: SprintComponent.Navigator,
    private val projectId: String,
    private val sprintId: String,
    private val sprintsRepo: SprintsRepo,
): ComponentContext by componentContext, SprintComponent {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow<SprintComponent.State>(SprintComponent.State.Loading)
    override val state = _state.asStateFlow()

    init {
        scope.launch {
            sprintsRepo.getSprint(sprintId).getOrNull().let {
                _state.emit(
                    if (it == null) SprintComponent.State.Error
                    else SprintComponent.State.Main(
                        sprintMeta = it.meta,
                        kanbanState = it.kanban
                    )
                )
            }
        }
    }

    override fun toKanban() {
        navigator.toKanban()
    }
}