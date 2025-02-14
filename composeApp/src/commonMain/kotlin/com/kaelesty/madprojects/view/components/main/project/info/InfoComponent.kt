package com.kaelesty.madprojects_kmp.blocs.project.info

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.project.Project
import com.kaelesty.madprojects.domain.repos.project.ProjectRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface InfoComponent {

    data class State(
        val project: Project? = null,
        val isLoading: Boolean = true
    )

    val state: StateFlow<State>

    interface Navigator {

        fun toUserProfile(userId: String)
    }

    interface Factory {

        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String
        ): InfoComponent
    }

    fun toUserProfile(userId: String)
}

class DefaultInfoComponent(
    private val componentContext: ComponentContext,
    private val navigator: InfoComponent.Navigator,
    private val projectId: String,
    private val projectRepo: ProjectRepo
): ComponentContext by componentContext, InfoComponent {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(InfoComponent.State())
    override val state: StateFlow<InfoComponent.State>
        get() = _state

    init {
        scope.launch {
            val result = projectRepo.getProject(projectId)
            _state.emit(
                InfoComponent.State(
                    project = result.getOrNull(),
                    isLoading = false
                )
            )
        }
    }

    override fun toUserProfile(userId: String) {
        navigator.toUserProfile(userId)
    }
}