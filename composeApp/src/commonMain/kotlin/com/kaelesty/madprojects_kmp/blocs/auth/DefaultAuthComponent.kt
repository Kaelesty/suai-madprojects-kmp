package com.kaelesty.madprojects_kmp.blocs.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.register.RegisterComponent
import com.kaelesty.madprojects_kmp.blocs.welcome.WelcomeComponent
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class DefaultAuthComponent(
	private val componentContext: ComponentContext,
): ComponentContext by componentContext, AuthComponent {

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
				component = get (
					clazz = LoginComponent::class.java,
					parameters = { parametersOf(
						componentContext,
						object : LoginComponent.Navigator {
							override fun onSuccessfulLogin() {
								TODO("Not yet implemented")
							}

							override fun back() {
								navigation.pop()
							}
						}
					) }
				),
			)
		}

		Config.Welcome -> {
			AuthComponent.Child.Welcome(
				component = get(
					clazz = WelcomeComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							object : WelcomeComponent.Navigator {
								override fun toEnter() {
									navigation.pushToFront(Config.Login)
								}

								override fun toRegister() {
									navigation.pushToFront(Config.Register)
								}
							},
						)
					}
				)
			)
		}

		Config.Register -> {
			AuthComponent.Child.Register(
				component = get(
					clazz = RegisterComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							object : RegisterComponent.Navigator {
								override fun onSuccessfulRegister() {
									TODO("Not yet implemented")
								}

								override fun back() {
									navigation.pop()
								}
							}
						)
					}
				)
			)
		}
	}

	@Serializable
	private sealed interface Config {

		@Serializable
		data object Login: Config

		@Serializable
		data object Welcome: Config

		@Serializable
		data object Register: Config
	}
}