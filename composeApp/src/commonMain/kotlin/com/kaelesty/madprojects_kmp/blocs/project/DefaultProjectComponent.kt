package com.kaelesty.madprojects_kmp.blocs.project

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class DefaultProjectComponent(
	private val componentContext: ComponentContext,
	private val storeFactory: ProjectStoreFactory,
	projectId: Int
) : ComponentContext by componentContext, ProjectComponent {

	private val navigation = StackNavigation<Config>()

	private val scope = CoroutineScope(Dispatchers.IO)

	private val store = instanceKeeper.getStore {
		storeFactory
			.create(projectId)
			.apply {
				scope.launch {
					labels.collect {
						handleLabel(it)
					}
				}
			}
	}

	override val state: StateFlow<ProjectStore.State>
		get() = store.stateFlow

	override fun navigate(target: ProjectComponent.Child.NavTarget) {
		store.accept(ProjectStore.Intent.Navigate(target))
	}

	override val stack: Value<ChildStack<*, ProjectComponent.Child>> = childStack(
		source = navigation,
		initialConfiguration = Config.Activity,
		handleBackButton = true,
		serializer = Config.serializer(),
		childFactory = ::child
	)

	private fun handleLabel(label: ProjectStore.Label) {
		navigation.pushToFront(
			configuration =  when (label) {
				ProjectStore.Label.NavigateToActivity -> Config.Activity
				ProjectStore.Label.NavigateToInfo -> Config.Info
				ProjectStore.Label.NavigateToKanban -> Config.Kanban
				ProjectStore.Label.NavigateToMessenger -> Config.Messenger
				ProjectStore.Label.NavigateToSettings -> Config.Settings
			}
		)
	}

	private fun child(
		config: Config,
		componentContext: ComponentContext,
	): ProjectComponent.Child = when (config) {
		Config.Activity -> {
			ProjectComponent.Child.Activity(
				component = get(
					clazz = ActivityComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							// TODO NAVIGATOR
						)
					}
				)
			)
		}

		Config.Info -> {
			ProjectComponent.Child.Info(
				component = get(
					clazz = InfoComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							// TODO NAVIGATOR
						)
					}
				)
			)
		}
		Config.Kanban -> {
			ProjectComponent.Child.Kanban(
				component = get(
					clazz = KanbanComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							// TODO NAVIGATOR
						)
					}
				)
			)
		}
		Config.Messenger -> {
			ProjectComponent.Child.Messenger(
				component = get(
					clazz = MessengerComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							// TODO NAVIGATOR
						)
					}
				)
			)
		}
		Config.Settings -> {
			ProjectComponent.Child.Settings(
				component = get(
					clazz = SettingsComponent::class.java,
					parameters = {
						parametersOf(
							componentContext,
							// TODO NAVIGATOR
						)
					}
				)
			)
		}
	}

	@Serializable
	private sealed interface Config {
		@Serializable
		data object Activity : Config

		@Serializable
		data object Messenger : Config

		@Serializable
		data object Kanban : Config

		@Serializable
		data object Info : Config

		@Serializable
		data object Settings : Config
	}
}