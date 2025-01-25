package com.kaelesty.madprojects.view.components.main.project.activity

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.github.GithubRepo
import com.kaelesty.madprojects.domain.repos.github.RepoView
import com.kaelesty.madprojects.domain.repos.sprints.ProfileSprint
import com.kaelesty.madprojects.domain.repos.sprints.SprintsRepo
import com.kaelesty.madprojects.view.components.main.project.ProjectComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface ActivityComponent {

    data class State(
        val sprints: List<ProfileSprint>?,
        val repoBranches: List<RepoView>?,
    )

    interface Navigator {

        fun toSprint(sprintId: String)

        fun toSprintCreation()
    }

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): ActivityComponent
    }

    val state: StateFlow<State?>

    fun toSprint(sprintId: String)

    fun toSprintCreation()
}

class DefaultActivityComponent(
    private val componentContext: ComponentContext,
    private val navigator: ActivityComponent.Navigator,
    private val projectId: String,
    private val sprintsRepo: SprintsRepo,
    private val githubRepo: GithubRepo,
): ComponentContext by componentContext, ActivityComponent  {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow<ActivityComponent.State?>(null)
    override val state: StateFlow<ActivityComponent.State?>
        get() = _state.asStateFlow()

    init {
        loadState()
    }

    private fun loadState() {
        scope.launch {
            _state.emit(
                ActivityComponent.State(
                    sprints = sprintsRepo.getProjectSprints(projectId).getOrNull(),
                    repoBranches = githubRepo.getProjectRepoBranches(projectId).getOrNull()
                )
            )
        }
    }

    override fun toSprint(sprintId: String) = navigator.toSprint(sprintId)

    override fun toSprintCreation() = navigator.toSprintCreation()
}