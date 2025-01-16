package com.kaelesty.madprojects.view.components.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.WelcomeComponent
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf

interface AuthComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        class Login(val component: LoginComponent): Child

        class Welcome(val component: WelcomeComponent): Child

        //class Register(val component: RegisterComponent): Child
    }

    interface Factory {
        fun create(c: ComponentContext): AuthComponent
    }
}

class DefaultAuthComponent(
    componentContext: ComponentContext,
    private val welcomeComponentFactory: WelcomeComponent.Factory,
    private val loginComponentFactory: LoginComponent.Factory,
): AuthComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AuthComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Welcome,
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): AuthComponent.Child = when (config) {
        is Config.Login -> {
            AuthComponent.Child.Login(
                component = loginComponentFactory.create(
                    componentContext,
                    object : LoginComponent.Navigator {
                        override fun back() {
                            navigation.pop()
                        }
                    }
                )
            )
        }

        Config.Welcome -> {
            AuthComponent.Child.Welcome(
                component = welcomeComponentFactory.create(
                    componentContext,
                    object : WelcomeComponent.Navigator {
                        override fun toEnter() {
                            navigation.pushToFront(Config.Login)
                        }

                        override fun toRegister() {
                            //navigation.pushToFront(Config.Register)
                        }
                    },
                )
            )
        }

//        Config.Register -> {
//            AuthComponent.Child.Register(
//                component = get(
//                    clazz = RegisterComponent::class.java,
//                    parameters = {
//                        parametersOf(
//                            componentContext,
//                            object : RegisterComponent.Navigator {
//
//                                override fun back() {
//                                    navigation.pop()
//                                }
//                            }
//                        )
//                    }
//                )
//            )
//        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Login: Config

        @Serializable
        data object Welcome: Config

//        @Serializable
//        data object Register: Config
    }
}