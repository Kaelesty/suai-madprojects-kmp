package com.kaelesty.madprojects.view.components.main.project

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.kaelesty.madprojects.domain.repos.profile.ProfileProject
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import com.kaelesty.madprojects.view.components.main.DefaultMainComponent.Config
import com.kaelesty.madprojects.view.components.main.MainComponent.Child
import com.kaelesty.madprojects.view.components.main.project.activity.ActivityComponent
import com.kaelesty.madprojects.view.components.main.project.kanban.KanbanComponent
import com.kaelesty.madprojects.view.components.main.project.sprint.SprintComponent
import com.kaelesty.madprojects.view.components.main.project.sprint_creation.SprintCreationComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.activity_nav
import madprojects.composeapp.generated.resources.info_nav
import madprojects.composeapp.generated.resources.kanban_nav
import madprojects.composeapp.generated.resources.messenger_nav
import madprojects.composeapp.generated.resources.settings_nav
import org.jetbrains.compose.resources.DrawableResource

interface ProjectComponent {

    data class State(
        val projectName: String = "Название"
    )

    sealed interface Child {

        data class Activity(val component: ActivityComponent): Child

        data class Messenger(val component: MessengerComponent): Child

        data class Info(val component: InfoComponent): Child

        data class Kanban(val component: KanbanComponent): Child

        data class Settings(val component: SettingsComponent): Child

        data class Sprint(val component: SprintComponent): Child

        data class SprintCreation(val component: SprintCreationComponent): Child

        enum class NavTarget(
            val title: String,
            val icon: DrawableResource,
            val iconScale: Float
        ) {
            Activity("Активность", Res.drawable.activity_nav, 0.9f),
            Messenger("Мессенджер", Res.drawable.messenger_nav, 0.85f),
            Info("Информация", Res.drawable.info_nav, 0.85f),
            Kanban("Канбан", Res.drawable.kanban_nav, 0.85f),
            Settings("Настройки", Res.drawable.settings_nav, 0.9f),
        }
    }

    interface Navigator {
        // todo
    }

    interface Factory {

        fun create(
            c: ComponentContext,
            n: Navigator,
            project: ProfileProject
        ): ProjectComponent
    }

    val stack: Value<ChildStack<*, Child>>

    val state: StateFlow<State>

    fun setChild(new: Child.NavTarget)
}

class DefaultProjectComponent(
    private val componentContext: ComponentContext,
    private val navigator: ProjectComponent.Navigator,
    private val project: ProfileProject,
    private val activityComponentFactory: ActivityComponent.Factory,
    private val messengerComponentFactory: MessengerComponent.Factory,
    private val infoComponentFactory: InfoComponent.Factory,
    private val kanbanComponentFactory: KanbanComponent.Factory,
    private val settingsComponentFactory: SettingsComponent.Factory,
    private val sprintComponentFactory: SprintComponent.Factory,
    private val sprintCreationComponentFactory: SprintCreationComponent.Factory,

    private val socketRepository: SocketRepository
): ProjectComponent, ComponentContext by componentContext {

    @Serializable
    sealed interface Config {

        @Serializable data class Activity(val projectId: String): Config

        @Serializable data class Messenger(val projectId: String): Config

        @Serializable data class Info(val projectId: String): Config

        @Serializable data class Kanban(val projectId: String): Config

        @Serializable data class Settings(val projectId: String): Config

        @Serializable data class Sprint(val projectId: String, val sprintId: String): Config

        @Serializable data class SprintCreation(val projectId: String): Config
    }

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, ProjectComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Activity(project.id),
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private val _state = MutableStateFlow(ProjectComponent.State(
        projectName = project.title
    ))

    override val state: StateFlow<ProjectComponent.State>
        get() = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {

        lifecycle.doOnCreate {
            scope.launch {
                socketRepository.start {
                    socketRepository.accept(
                        com.kaelesty.madprojects.domain.repos.socket.Intent.Messenger.Start(
                            projectId = project.id.toInt()
                        )
                    )
                    socketRepository.accept(
                        com.kaelesty.madprojects.domain.repos.socket.Intent.Kanban.Start(
                            projectId = project.id.toInt()
                        )
                    )
                    socketRepository.accept(
                        com.kaelesty.madprojects.domain.repos.socket.Intent.Messenger.RequestChatsList(
                            projectId = project.id.toInt()
                        )
                    )
                }

            }
        }

        lifecycle.doOnDestroy {
            scope.launch { socketRepository.stop() }
        }
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): ProjectComponent.Child = when(config) {
        is Config.Activity -> ProjectComponent.Child.Activity(
            activityComponentFactory.create(
                c = componentContext,
                n = object : ActivityComponent.Navigator {
                    override fun toSprint(sprintId: String) {
                        navigation.push(Config.Sprint(project.id, sprintId))
                    }

                    override fun toSprintCreation() {
                        navigation.push(Config.SprintCreation(project.id))
                    }
                },
                projectId = project.id
            )
        )
        is Config.Info -> ProjectComponent.Child.Info(
            component = infoComponentFactory.create(
                componentContext, object : InfoComponent.Navigator {

                }, project.id
            )
        )
        is Config.Kanban -> ProjectComponent.Child.Kanban(
            component = kanbanComponentFactory.create(
                componentContext, object : KanbanComponent.Navigator {

                }, project.id
            )
        )
        is Config.Messenger -> ProjectComponent.Child.Messenger(
            component = messengerComponentFactory.create(
                componentContext, projectId = project.id
            )
        )
        is Config.Settings -> ProjectComponent.Child.Settings(
            component = settingsComponentFactory.create(
                componentContext, object : SettingsComponent.Navigator {

                }, project.id
            )
        )

        is Config.Sprint -> ProjectComponent.Child.Sprint(
            component = sprintComponentFactory.create(
                componentContext, object: SprintComponent.Navigator {

                }, config.projectId, config.sprintId
            )
        )
        is Config.SprintCreation -> ProjectComponent.Child.SprintCreation(
            component = sprintCreationComponentFactory.create(
                componentContext, object: SprintCreationComponent.Navigator {

                }, project.id
            )
        )
    }

    override fun setChild(new: ProjectComponent.Child.NavTarget) {
        navigation.bringToFront(
            when (new) {
                ProjectComponent.Child.NavTarget.Activity -> Config.Activity(project.id)
                ProjectComponent.Child.NavTarget.Messenger -> Config.Messenger(project.id)
                ProjectComponent.Child.NavTarget.Info -> Config.Info(project.id)
                ProjectComponent.Child.NavTarget.Kanban -> Config.Kanban(project.id)
                ProjectComponent.Child.NavTarget.Settings -> Config.Settings(project.id)
            }
        )
    }
}