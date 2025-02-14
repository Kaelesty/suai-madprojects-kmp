package com.kaelesty.madprojects.view.components.main.project.kanban

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.socket.KanbanState
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface KanbanComponent {

    data class State(
        val isLoading: Boolean = true,
        val state: KanbanState = KanbanState(listOf())
    )

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): KanbanComponent
    }

    val state: StateFlow<State>
}

class DefaultKanbanComponent(
    private val componentContext: ComponentContext,
    private val navigator: KanbanComponent.Navigator,
    private val projectId: String,
    private val socketRepository: SocketRepository,
): ComponentContext by componentContext, KanbanComponent {

    private val _state = MutableStateFlow(KanbanComponent.State())
    override val state = _state.asStateFlow()
}