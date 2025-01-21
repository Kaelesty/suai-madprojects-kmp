package com.kaelesty.madprojects.view.components.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects.domain.repos.auth.User
import com.kaelesty.madprojects.view.components.main.MainComponent.Child
import com.kaelesty.madprojects.view.components.main.profile.ProfileComponent
import com.kaelesty.madprojects.view.components.main.project.ProjectComponent
import com.kaelesty.madprojects.view.components.main.project_creation.ProjectCreationComponent
import kotlinx.serialization.Serializable

interface MainComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Profile(val component: ProfileComponent): Child

        data class ProjectCreation(val component: ProjectCreationComponent): Child

        data class Project(val component: ProjectComponent): Child
    }

    interface Factory {
        fun create(
            c: ComponentContext,
            user: User,
        ): MainComponent
    }

}

class DefaultMainComponent(
    private val componentContext: ComponentContext,
    private val user: User,
    private val profileComponentFactory: ProfileComponent.Factory,
    private val projectCreationComponentFactory: ProjectCreationComponent.Factory,
    private val projectComponentFactory: ProjectComponent.Factory,
): MainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Profile,
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): Child = when(config) {
        Config.Profile -> Child.Profile(
            component = profileComponentFactory.create(
                componentContext,
                object: ProfileComponent.Navigator {

                    override fun toProjectCreation() {
                        navigation.push(Config.ProjectCreation)
                    }
                }
            )
        )

        Config.ProjectCreation -> Child.ProjectCreation(
            component = projectCreationComponentFactory.create(
                componentContext,
                object : ProjectCreationComponent.Navigator {
                    override fun onFinish(projectId: String) {
                        navigation.push(Config.Project(projectId))
                    }
                }
            )
        )

        is Config.Project -> Child.Project(
            component = projectComponentFactory.create(
                componentContext,
                object : ProjectComponent.Navigator {
                    // todo
                },
                config.projectId,
            )
        )
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Profile: Config

        @Serializable
        data object ProjectCreation: Config

        @Serializable
        data class Project(val projectId: String): Config
    }
}