package com.kaelesty.madprojects.view.components.main.project

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects.view.components.main.DefaultMainComponent.Config
import com.kaelesty.madprojects.view.components.main.MainComponent.Child
import com.kaelesty.madprojects.view.components.main.project.activity.ActivityComponent
import com.kaelesty.madprojects.view.components.main.project.kanban.KanbanComponent
import com.kaelesty.madprojects.view.components.main.project.messenger.MessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            projectId: String
        ): ProjectComponent
    }

    val stack: Value<ChildStack<*, Child>>

    val state: StateFlow<State>

    fun setChild(new: Child.NavTarget)
}

class DefaultProjectComponent(
    private val componentContext: ComponentContext,
    private val navigator: ProjectComponent.Navigator,
    private val projectId: String,
    private val activityComponentFactory: ActivityComponent.Factory,
    private val messengerComponentFactory: MessengerComponent.Factory,
    private val infoComponentFactory: InfoComponent.Factory,
    private val kanbanComponentFactory: KanbanComponent.Factory,
    private val settingsComponentFactory: SettingsComponent.Factory
): ProjectComponent, ComponentContext by componentContext {

    @Serializable
    sealed interface Config {

        @Serializable data class Activity(val projectId: String): Config

        @Serializable data class Messenger(val projectId: String): Config

        @Serializable data class Info(val projectId: String): Config

        @Serializable data class Kanban(val projectId: String): Config

        @Serializable data class Settings(val projectId: String): Config
    }

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, ProjectComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Activity(projectId),
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private val _state = MutableStateFlow(ProjectComponent.State())
    override val state: StateFlow<ProjectComponent.State>
        get() = _state.asStateFlow()

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): ProjectComponent.Child = when(config) {
        is Config.Activity -> ProjectComponent.Child.Activity(
            activityComponentFactory.create(
                c = componentContext,
                n = object : ActivityComponent.Navigator {

                },
                projectId = projectId
            )
        )
        is Config.Info -> ProjectComponent.Child.Info(
            component = infoComponentFactory.create(
                componentContext, object : InfoComponent.Navigator {

                }, projectId
            )
        )
        is Config.Kanban -> ProjectComponent.Child.Kanban(
            component = kanbanComponentFactory.create(
                componentContext, object : KanbanComponent.Navigator {

                }, projectId
            )
        )
        is Config.Messenger -> ProjectComponent.Child.Messenger(
            component = messengerComponentFactory.create(
                componentContext, object : MessengerComponent.Navigator {

                }, projectId
            )
        )
        is Config.Settings -> ProjectComponent.Child.Settings(
            component = settingsComponentFactory.create(
                componentContext, object : SettingsComponent.Navigator {

                }, projectId
            )
        )
    }

    override fun setChild(new: ProjectComponent.Child.NavTarget) {
        navigation.bringToFront(
            when (new) {
                ProjectComponent.Child.NavTarget.Activity -> Config.Activity(projectId)
                ProjectComponent.Child.NavTarget.Messenger -> Config.Messenger(projectId)
                ProjectComponent.Child.NavTarget.Info -> Config.Info(projectId)
                ProjectComponent.Child.NavTarget.Kanban -> Config.Kanban(projectId)
                ProjectComponent.Child.NavTarget.Settings -> Config.Settings(projectId)
            }
        )
    }
}