package com.kaelesty.madprojects_kmp.blocs.memberProfile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.memberProfile.profile.ProfileComponent
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class DefaultMemberProfileComponent(
    private val componentContext: ComponentContext,
): ComponentContext by componentContext, MemberProfileComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MemberProfileComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = Config.Profile,
            handleBackButton = true,
            serializer = Config.serializer(),
            childFactory = ::child
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): MemberProfileComponent.Child = when (config) {
        Config.NewProject -> {
            MemberProfileComponent.Child.NewProject(
                component = get(
                    clazz = ProfileComponent::class.java,
                    parameters = {
                        parametersOf(componentContext)
                    }
                )
            )
        }
        Config.Profile -> {
            MemberProfileComponent.Child.Profile(
                component = get(
                    clazz = ProfileComponent::class.java,
                    parameters = {
                        parametersOf(componentContext)
                    }
                )
            )
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object NewProject : Config

        @Serializable
        data object Profile : Config
    }
}