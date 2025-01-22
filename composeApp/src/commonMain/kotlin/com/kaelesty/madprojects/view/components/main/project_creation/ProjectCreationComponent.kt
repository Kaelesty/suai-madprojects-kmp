package com.kaelesty.madprojects.view.components.main.project_creation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects.domain.repos.curatorship.AvailableCurator
import com.kaelesty.madprojects.domain.repos.project.ProjectGroup
import com.kaelesty.madprojects.domain.stores.ProjectCreationStore
import com.kaelesty.madprojects.domain.stores.ProjectCreationStoreFactory
import com.kaelesty.madprojects.view.components.main.project.ProjectComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface ProjectCreationComponent {
    fun updateTitle(it: String)

    fun updateDesc(it: String)

    fun updateMaxMembersCount(it: Int)

    fun selectCurator(it: AvailableCurator)

    fun selectProjectGroup(it: ProjectGroup)

    fun submit()

    fun removeRepoLink(it: String)

    val state: StateFlow<ProjectCreationStore.State>
    val repoLinkState: StateFlow<RepoLinkState>

    sealed interface RepoLinkState {
        data object Finished: RepoLinkState
        data object Error: RepoLinkState
    }

    interface Navigator {

        fun onFinish(projectId: String)
    }

    interface Factory {

        fun create(
            c: ComponentContext,
            n: Navigator
        ): ProjectCreationComponent
    }
}

class DefaultProjectCreationComponent(
    private val componentContext: ComponentContext,
    private val navigator: ProjectCreationComponent.Navigator,
    storeFactory: ProjectCreationStoreFactory,
): ProjectCreationComponent, ComponentContext by componentContext {

    val scope = CoroutineScope(Dispatchers.Main)

    val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override val repoLinkState: StateFlow<ProjectCreationComponent.RepoLinkState>
        get() = TODO("Not yet implemented")

    override fun updateTitle(it: String) {
        store.accept(ProjectCreationStore.Intent.UpdateTitle(it))
    }

    override fun updateDesc(it: String) {
        store.accept(ProjectCreationStore.Intent.UpdateDesc(it))
    }

    override fun updateMaxMembersCount(it: Int) {
        store.accept(ProjectCreationStore.Intent.UpdateMaxMembersCount(it))
    }

    override fun selectCurator(it: AvailableCurator) {
        store.accept(ProjectCreationStore.Intent.UpdateCurator(it))
    }

    override fun selectProjectGroup(it: ProjectGroup) {
        store.accept(ProjectCreationStore.Intent.UpdateProjectGroup(it))
    }

    override fun submit() {
        store.accept(ProjectCreationStore.Intent.Create)
    }

    override fun removeRepoLink(it: String) {
        store.accept(ProjectCreationStore.Intent.RemoveRepoLink(it))
    }
}