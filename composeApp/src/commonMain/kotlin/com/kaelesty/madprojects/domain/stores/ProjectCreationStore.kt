package com.kaelesty.madprojects.domain.stores

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects.domain.PROJECT_DESC_MAX_LENGTH
import com.kaelesty.madprojects.domain.PROJECT_TITLE_MAX_LENGTH
import com.kaelesty.madprojects.domain.repos.project.ProjectRepo
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

        data class UpdateCuratorId(val new: String): Intent

        data class UpdateRepoLink(val new: String): Intent

        data object ConfirmRepoLink: Intent

        data class UpdateProjectGroupId(val new: String): Intent

        data class RemoveRepoLink(val index: Int): Intent
    }

    data class State(
        val title: String = "",
        val maxMembersCount: Int = 3,
        val desc: String = "",
        val curatorId: String? = null,
        val repoLinks: List<String> = listOf(),
        val repoLink: String = "",
        val projectGroupId: String? = "",
        val isLoading: Boolean = true,
    ): ProfileStore.Intent {

    }

    sealed interface Label {

        data class Finished(val projectId: String): Label

        data object CreationError: Label

        data object EmptyField: Label
    }
}

class ProjectCreationStoreFactory(
    private val storeFactory: StoreFactory,
    private val projectRepo: ProjectRepo,
) {

    fun create(): ProjectCreationStore =
        object : ProjectCreationStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProjectCreationStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(projectRepo) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class UpdateTitle(val new: String): Msg

        data class UpdateMaxMembersCount(val new: Int): Msg

        data class UpdateDesc(val new: String): Msg

        data class UpdateCuratorId(val new: String): Msg

        data class UpdateRepoLink(val new: String): Msg

        data object ConfirmRepoLink: Msg

        data class RemoveRepoLink(val index: Int): Msg

        data class UpdateLoading(val new: Boolean): Msg

        data class UpdateProjectGroupId(val new: String): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl(
        private val projectRepo: ProjectRepo,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {

                is Intent.UpdateProjectGroupId -> dispatch(Msg.UpdateProjectGroupId(intent.new))

                is Intent.ConfirmRepoLink -> {
                    // TODO verify
                    dispatch(Msg.ConfirmRepoLink)
                }

                Intent.Create -> {
                    dispatch(Msg.UpdateLoading(true))
                    scope.launch {
                        with(state()) {
                            if (curatorId == null || projectGroupId == null) {
                                publish(Label.EmptyField)
                                return@launch
                            }
                            val res = projectRepo.createProject(
                                title = title.ifBlank { publish(Label.EmptyField); return@launch },
                                maxMembersCount = maxMembersCount,
                                desc = desc.ifBlank { publish(Label.EmptyField); return@launch },
                                curatorId = curatorId.ifBlank { publish(Label.EmptyField); return@launch },
                                repoLinks = repoLinks,
                                projectGroupId = projectGroupId
                            )

                            res.getOrNull()?.let {
                                publish(Label.Finished(it))
                            } ?: publish(Label.CreationError)
                        }
                    }
                }
                is Intent.RemoveRepoLink -> dispatch(Msg.RemoveRepoLink(intent.index))
                is Intent.UpdateCuratorId -> dispatch(Msg.UpdateCuratorId(intent.new))
                is Intent.UpdateDesc -> dispatch(Msg.UpdateDesc(
                    intent.new.take(PROJECT_DESC_MAX_LENGTH)
                ))
                is Intent.UpdateMaxMembersCount -> dispatch(Msg.UpdateMaxMembersCount(intent.new.coerceIn(1, 10)))
                is Intent.UpdateTitle -> dispatch(Msg.UpdateTitle(
                    intent.new.take(PROJECT_TITLE_MAX_LENGTH)
                ))

                is Intent.UpdateRepoLink -> dispatch(Msg.UpdateRepoLink(intent.new))
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.ConfirmRepoLink -> copy(
                    repoLinks = repoLinks
                        .toMutableList()
                        .apply { add(repoLink) }
                        .toList(),
                    repoLink = ""
                )
                is Msg.UpdateRepoLink -> copy(
                    repoLink = message.new
                )
                is Msg.RemoveRepoLink -> copy(
                    repoLinks = repoLinks
                        .toMutableList()
                        .apply { removeAt(message.index) }
                        .toList()
                )
                is Msg.UpdateCuratorId -> copy(curatorId = message.new)
                is Msg.UpdateDesc -> copy(desc = message.new)
                is Msg.UpdateMaxMembersCount -> copy(maxMembersCount = message.new)
                is Msg.UpdateTitle -> copy(title = message.new)
                is Msg.UpdateLoading -> copy(isLoading = message.new)
                is Msg.UpdateProjectGroupId -> copy(projectGroupId = message.new)
            }
    }
}
