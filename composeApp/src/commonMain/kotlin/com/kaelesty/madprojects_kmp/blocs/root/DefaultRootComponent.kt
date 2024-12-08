package com.kaelesty.madprojects_kmp.blocs.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.profile.ProfileComponent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectComponent
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

    override fun setAuthorized(boolean: Boolean) {
        navigation.replaceAll(if (boolean) Config.Profile else Config.Auth)
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {

        is Config.Project -> {
            RootComponent.Child.Project(
                component = get(
                    clazz = ProjectComponent::class.java,
                    parameters = {
                        parametersOf(componentContext, config.projectId, config.jwt)
                    }
                )
            )
        }



        is Config.Profile -> {
            RootComponent.Child.Profile(
                component = get(
                    clazz = ProfileComponent::class.java,
                    parameters = {
                        parametersOf(
                            componentContext,
                            object : ProfileComponent.Navigator {
                                override fun editProfile() {
                                    TODO("Not yet implemented")
                                }

                                override fun openProject(projectId: Int) {
                                    TODO("Not yet implemented")
                                }

                                override fun connectProject() {
                                    TODO("Not yet implemented")
                                }

                                override fun createProject() {
                                    TODO("Not yet implemented")
                                }
                            }
                        )
                    }
                )
            )
        }

        Config.Auth -> {
            RootComponent.Child.Auth(
                component = get(
                    clazz = AuthComponent::class.java,
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
        data class Project(val projectId: Int, val jwt: String) : Config

        @Serializable
        data object Profile : Config

        @Serializable
        data object Auth: Config
    }
}