package com.kaelesty.madprojects.domain.stores

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.PROJECT_DESC_MAX_LENGTH
import com.kaelesty.madprojects.domain.PROJECT_TITLE_MAX_LENGTH
import com.kaelesty.madprojects.domain.repos.curatorship.AvailableCurator
import com.kaelesty.madprojects.domain.repos.curatorship.CuratorshipRepo
import com.kaelesty.madprojects.domain.repos.profile.ProfileProject
import com.kaelesty.madprojects.domain.repos.project.ProjectGroup
import com.kaelesty.madprojects.domain.repos.project.ProjectRepo
import com.kaelesty.madprojects.domain.repos.project.ProjectStatus
import com.kaelesty.madprojects.domain.stores.ProjectCreationStore.Intent
import com.kaelesty.madprojects.domain.stores.ProjectCreationStore.Label
import com.kaelesty.madprojects.domain.stores.ProjectCreationStore.State
import kotlinx.coroutines.launch

interface ProjectCreationStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object Create: Intent

        data class UpdateTitle(val new: String): Intent

        data class UpdateMaxMembersCount(val new: Int): Intent

        data class UpdateDesc(val new: String): Intent

        data class UpdateCurator(val new: AvailableCurator): Intent

        data class AddRepoLink(val new: String): Intent

        data class UpdateProjectGroup(val new: ProjectGroup): Intent

        data class RemoveRepoLink(val repoLink: String): Intent
    }

    data class State(
        val title: String = "",
        val maxMembersCount: Int = 3,
        val desc: String = "",
        val curator: AvailableCurator? = null,
        val repoLinks: List<String> = listOf(),
        val projectGroup: ProjectGroup? = null,
        val isLoading: Boolean = true,

        val curators: List<AvailableCurator> = listOf(),
        val projectGroups: List<ProjectGroup> = listOf()
    )

    sealed interface Label {

        data class Finished(val project: ProfileProject): Label

        data object CreationError: Label

        data object EmptyField: Label

        data object RepolinkValidated: Label

        data object BadRepolink: Label
    }
}

class ProjectCreationStoreFactory(
    private val storeFactory: StoreFactory,
    private val projectRepo: ProjectRepo,
    private val curatorshipRepo: CuratorshipRepo,
) {

    fun create(): ProjectCreationStore =
        object : ProjectCreationStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProjectCreationStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(projectRepo, curatorshipRepo) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object UpdateCuratorsList: Action
    }

    private sealed interface Msg {

        data class UpdateTitle(val new: String): Msg

        data class UpdateMaxMembersCount(val new: Int): Msg

        data class UpdateDesc(val new: String): Msg

        data class UpdateCurator(val new: AvailableCurator): Msg

        data class AddRepoLink(val new: String): Msg

        data class RemoveRepoLink(val repoLink: String): Msg

        data class UpdateLoading(val new: Boolean): Msg

        data class UpdateProjectGroup(val new: ProjectGroup): Msg

        data class UpdateProjectGroups(val new: List<ProjectGroup>): Msg

        data class UpdateCuratorsList(val new: List<AvailableCurator>): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.UpdateCuratorsList)
        }
    }

    private class ExecutorImpl(
        private val projectRepo: ProjectRepo,
        private val curatorshipRepo: CuratorshipRepo,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {

                is Intent.UpdateProjectGroup -> dispatch(Msg.UpdateProjectGroup(intent.new))

                Intent.Create -> {
                    dispatch(Msg.UpdateLoading(true))
                    scope.launch {
                        with(state()) {
                            if (curator == null || projectGroup == null) {
                                publish(Label.EmptyField)
                                return@launch
                            }
                            val res = projectRepo.createProject(
                                title = title.ifBlank { publish(Label.EmptyField); return@launch },
                                maxMembersCount = maxMembersCount,
                                desc = desc.ifBlank { publish(Label.EmptyField); return@launch },
                                curatorId = curator.id,
                                repoLinks = repoLinks,
                                projectGroupId = projectGroup.id
                            )

                            res.getOrNull()?.let {
                                publish(Label.Finished(
                                    ProfileProject(
                                        id = it, title = state().title, mark = null, status = ProjectStatus.Pending
                                    )
                                ))
                            } ?: publish(Label.CreationError)
                        }
                    }
                }
                is Intent.RemoveRepoLink -> dispatch(Msg.RemoveRepoLink(intent.repoLink))
                is Intent.UpdateCurator -> {
                    dispatch(Msg.UpdateCurator(intent.new))
                    updateProjectGroups(intent.new.id)
                }
                is Intent.UpdateDesc -> dispatch(Msg.UpdateDesc(
                    intent.new.take(PROJECT_DESC_MAX_LENGTH)
                ))
                is Intent.UpdateMaxMembersCount -> dispatch(Msg.UpdateMaxMembersCount(intent.new.coerceIn(1, 10)))
                is Intent.UpdateTitle -> dispatch(Msg.UpdateTitle(
                    intent.new.take(PROJECT_TITLE_MAX_LENGTH)
                ))

                is Intent.AddRepoLink -> {
                    scope.launch {
                        if (projectRepo.validateRepolink(intent.new)) {
                            dispatch(Msg.AddRepoLink(intent.new))
                            publish(Label.RepolinkValidated)
                        }
                        else {
                            publish(Label.BadRepolink)
                        }
                    }

                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.UpdateCuratorsList -> {
                    scope.launch {
                        curatorshipRepo.getCuratorsList().let {
                            it.getOrNull()?.let {
                                dispatch(Msg.UpdateCuratorsList(it))
                            } ?:
                            it.exceptionOrNull().toString()
                        }
                    }
                }
            }
        }

        private fun updateProjectGroups(curatorId: String) {
            scope.launch {
                dispatch(Msg.UpdateProjectGroups(listOf()))
                curatorshipRepo.getCuratorProjectGroups(curatorId).getOrNull()?.let {
                    dispatch(Msg.UpdateProjectGroups(it))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.AddRepoLink -> copy(
                    repoLinks = repoLinks
                        .toMutableList()
                        .apply { add(message.new) }
                        .toList(),
                )
                is Msg.RemoveRepoLink -> copy(
                    repoLinks = repoLinks
                        .toMutableList()
                        .apply { remove(message.repoLink) }
                        .toList()
                )
                is Msg.UpdateCurator -> copy(curator = message.new)
                is Msg.UpdateDesc -> copy(desc = message.new)
                is Msg.UpdateMaxMembersCount -> copy(maxMembersCount = message.new)
                is Msg.UpdateTitle -> copy(title = message.new)
                is Msg.UpdateLoading -> copy(isLoading = message.new)
                is Msg.UpdateProjectGroup -> copy(projectGroup = message.new)
                is Msg.UpdateCuratorsList -> copy(curators = message.new, curator = null)
                is Msg.UpdateProjectGroups -> copy(projectGroups = message.new, projectGroup = null)
            }
    }
}
