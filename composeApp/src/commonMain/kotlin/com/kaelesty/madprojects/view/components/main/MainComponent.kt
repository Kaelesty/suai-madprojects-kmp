package com.kaelesty.madprojects.view.components.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects.domain.stores.root.User
import com.kaelesty.madprojects.view.components.main.MainComponent.Child
import com.kaelesty.madprojects.view.components.main.profile.ProfileComponent
import kotlinx.serialization.Serializable

interface MainComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Profile(val component: ProfileComponent): Child

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
            component = profileComponentFactory.create(componentContext)
        )
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Profile: Config
    }
}