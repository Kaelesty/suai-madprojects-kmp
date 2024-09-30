package com.kaelesty.madprojects_kmp.decompose.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.decompose.auth.AuthComponent
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class DefaultRootComponent(
	private val componentContext: ComponentContext,
): RootComponent, ComponentContext by componentContext {

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
					parameters = { parametersOf(componentContext) }
				)
			)
		}
	}

	@Serializable
	private sealed interface Config {

		@Serializable
		data object Auth: Config
	}
}