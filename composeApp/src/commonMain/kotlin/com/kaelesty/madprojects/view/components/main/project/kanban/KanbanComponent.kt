package com.kaelesty.madprojects.view.components.main.project.kanban

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStart
import com.kaelesty.madprojects.domain.repos.socket.Action
import com.kaelesty.madprojects.domain.repos.socket.Intent
import com.kaelesty.madprojects.domain.repos.socket.KanbanState
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

interface KanbanComponent {

    data class State(
        val isLoading: Boolean = true,
        val kanban: KanbanState = KanbanState(listOf()),
        val selectedColumnId: Int? = null,
    )

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): KanbanComponent
    }

    val state: StateFlow<State>

    fun selectColumn(id: Int)
}

class DefaultKanbanComponent(
    private val componentContext: ComponentContext,
    private val navigator: KanbanComponent.Navigator,
    private val projectId: String,
    private val socketRepository: SocketRepository,
) : ComponentContext by componentContext, KanbanComponent {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(KanbanComponent.State())
    override val state = _state.asStateFlow()

    init {
        lifecycle.doOnCreate {
            scope.launch {
                socketRepository.accept(Intent.Kanban.GetKanban(projectId.toInt()))
            }
        }

        scope.launch {
            socketRepository.action.collect {
                if (it is Action.Kanban) {
                    proceedAction(it)
                }
            }
        }
    }

    private suspend fun proceedAction(action: Action.Kanban) {
        when (action) {
            is Action.Kanban.SetState -> {
                _state.emit(
                    KanbanComponent.State(
                        isLoading = false,
                        kanban = action.kanban,
                        selectedColumnId = if (action.kanban.columns.isEmpty()) null
                            else action.kanban.columns.firstOrNull()?.id
                    )
                )
            }
        }
    }

    override fun selectColumn(id: Int) {
        scope.launch {
            _state.emit(
                _state.value.copy(
                    selectedColumnId = id
                )
            )
        }
    }
}