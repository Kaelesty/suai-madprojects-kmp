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

    fun editKard(kard: KanbanState.Kard, newTitle: String, newDesc: String)

    fun deleteKard(kardId: Int)

    fun upKard(kardId: Int)

    fun downKard(kardId: Int)

    fun setColumn(kardId: Int, newColumnId: Int, atStart: Boolean)

    fun createKard(name: String, columnId: Int)

    fun createColumn(name: String, color: String)

    fun upColumn(columnId: Int)

    fun downColumn(columnId: Int)

    fun deleteColumn(columnId: Int)
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
                val oldState = state.value
                _state.emit(
                    KanbanComponent.State(
                        isLoading = false,
                        kanban = action.kanban,
                        selectedColumnId = oldState.selectedColumnId ?: if (action.kanban.columns.isEmpty()) null
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

    override fun editKard(kard: KanbanState.Kard, newTitle: String, newDesc: String) {
        scope.launch { 
            socketRepository.accept(
                Intent.Kanban.UpdateKard(
                    id = kard.id,
                    name = if (kard.title == newTitle) null else newTitle,
                    desc = if (kard.desc == newDesc) null else newDesc,
                    projectId = projectId.toInt()
                )
            )
        }
    }

    override fun deleteKard(kardId: Int) {
        scope.launch { 
            socketRepository.accept(
                Intent.Kanban.DeleteKard(
                    id = kardId,
                    projectId = projectId.toInt()
                )
            )
        }
    }

    override fun upKard(kardId: Int) {
        scope.launch {
            val columns = _state.value.kanban.columns.map { it.id to it.kards.map { it.id } }
            columns.firstOrNull { it.second.contains(kardId) }?.let {
                val position = it.second.indexOf(kardId)
                if (position == 0) return@launch
                socketRepository.accept(
                    Intent.Kanban.MoveKard(
                        id = kardId,
                        columnId = it.first,
                        newColumnId = it.first,
                        newPosition = position - 1,
                        projectId = projectId.toInt()
                    )
                )
            }
        }
    }

    override fun downKard(kardId: Int) {
        scope.launch {
            val columns = _state.value.kanban.columns.map { it.id to it.kards.map { it.id } }
            columns.firstOrNull { it.second.contains(kardId) }?.let {
                val position = it.second.indexOf(kardId)
                if (position == it.second.size - 1) return@launch
                socketRepository.accept(
                    Intent.Kanban.MoveKard(
                        id = kardId,
                        columnId = it.first,
                        newColumnId = it.first,
                        newPosition = position + 1,
                        projectId = projectId.toInt()
                    )
                )
            }
        }
    }

    override fun setColumn(kardId: Int, newColumnId: Int, atStart: Boolean) {
        scope.launch {
            val columns = _state.value.kanban.columns.map { it.id to it.kards.map { it.id } }
            columns.firstOrNull { it.second.contains(kardId) }?.let {
                socketRepository.accept(
                    Intent.Kanban.MoveKard(
                        id = kardId,
                        columnId = it.first,
                        newColumnId = newColumnId,
                        newPosition = if (atStart) 0 else (columns.firstOrNull { it.first == newColumnId }?.second?.size?.minus(
                            1
                        )) ?: 0,
                        projectId = projectId.toInt()
                    )
                )
            }
        }
    }

    override fun createKard(name: String, columnId: Int) {
        scope.launch {
            socketRepository.accept(Intent.Kanban.CreateKard(
                name = name,
                desc = "",
                columnId = columnId,
                projectId = projectId.toInt()
            ))
        }
    }

    override fun createColumn(name: String, color: String) {
        scope.launch {
            socketRepository.accept(
                Intent.Kanban.CreateColumn(
                    name = name,
                    projectId = projectId.toInt(),
                    color = color
                )
            )
        }
    }

    override fun upColumn(columnId: Int) {
        scope.launch {
            val kanban = _state.value.kanban
            val index = kanban.columns
                .map { it.id }
                .indexOf(columnId)

            if (index == 0) return@launch

            socketRepository.accept(
                Intent.Kanban.MoveColumn(
                    id = columnId,
                    newPosition = index - 1,
                    projectId = projectId.toInt()
                )
            )
        }
    }

    override fun downColumn(columnId: Int) {
        scope.launch {
            val kanban = _state.value.kanban
            val index = kanban.columns
                .map { it.id }
                .indexOf(columnId)

            if (index == kanban.columns.size - 1) return@launch

            socketRepository.accept(
                Intent.Kanban.MoveColumn(
                    id = columnId,
                    newPosition = index + 1,
                    projectId = projectId.toInt()
                )
            )
        }
    }

    override fun deleteColumn(columnId: Int) {
        scope.launch {
            socketRepository.accept(
                Intent.Kanban.DeleteColumn(columnId, projectId.toInt())
            )
        }
    }
}