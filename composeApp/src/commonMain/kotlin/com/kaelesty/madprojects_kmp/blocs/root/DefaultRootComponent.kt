package com.kaelesty.madprojects_kmp.blocs.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.memberProfile.MemberProfileComponent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectComponent
import entities.UserType
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class DefaultRootComponent(
    private val componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Auth,
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {
        is Config.Auth -> {
            RootComponent.Child.Auth(
                component = get(
                    clazz = AuthComponent::class.java,
                    parameters = {
                        parametersOf(
                            componentContext,
                            object : AuthComponent.Navigator {
                                override fun onSuccessfulAuth(jwt: String, userType: UserType) {

                                    when (userType) {
                                        UserType.DEFAULT -> navigation.push(
                                            configuration = Config.MemberProfile(jwt)
                                        )

                                        UserType.CURATOR -> TODO()
                                    }
                                }
                            }
                        )
                    }
                )
            )
        }

        Config.Project -> {
            RootComponent.Child.Project(
                component = get(
                    clazz = ProjectComponent::class.java,
                    parameters = { parametersOf(componentContext) }
                )
            )
        }

        is Config.MemberProfile -> {
            RootComponent.Child.MemberProfile(
                component = get(
                    clazz = MemberProfileComponent::class.java,
                    parameters = {
                        parametersOf(
                            componentContext,
                            config.jwt
                        )
                    }
                )
            )
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Auth : Config

        @Serializable
        data object Project : Config

        @Serializable
        data class MemberProfile(val jwt: String) : Config
    }
}