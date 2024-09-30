package com.kaelesty.madprojects_kmp.decompose.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.decompose.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.decompose.auth.welcome.WelcomeComponent
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
						{}
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
									TODO()
								}
							},
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
	}
}