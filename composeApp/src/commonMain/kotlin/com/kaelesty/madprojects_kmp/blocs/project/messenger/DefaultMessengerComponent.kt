package com.kaelesty.madprojects_kmp.blocs.project.messenger

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chat.ChatComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.ChatsListComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class DefaultMessengerComponent(
	private val componentContext: ComponentContext,
	private val storeFactory: MessengerStoreFactory
): ComponentContext by componentContext, MessengerComponent {

	private val scope = CoroutineScope(Dispatchers.Main)

	private val navigation = StackNavigation<Config>()

	private val store = instanceKeeper.getStore {
		storeFactory.create().apply {
			scope.launch {
				labels.collect {
					handleLabel(it)
				}
			}
		}
	}

	override val stack: Value<ChildStack<*, MessengerComponent.Child>> = childStack(
		source = navigation,
		initialConfiguration = Config.ChatsList,
		handleBackButton = true,
		serializer = Config.serializer(),
		childFactory = ::child
	)

	override val state: StateFlow<MessengerStore.State>
		get() = store.stateFlow

	private fun child(
		config: Config,
		componentContext: ComponentContext,
	): MessengerComponent.Child = when (config) {
		is Config.Chat -> MessengerComponent.Child.Chat(
			component = get(
				clazz = ChatComponent::class.java,
				parameters = {
					parametersOf(
						componentContext,
						store,
						config.chatId,
					)
				},
			)
		)
		Config.ChatsList -> MessengerComponent.Child.ChatsList(
			component = get(
				clazz = ChatsListComponent::class.java,
				parameters = {
					parametersOf(
						componentContext,
						store,
						::onChatSelected
					)
				},
			)
		)
	}

	private fun handleLabel(label: MessengerStore.Label) {
		when (label) {
			is MessengerStore.Label.NavigateToChat -> {
				navigation.pushToFront(Config.Chat(label.chatId))
			}
		}
	}

	private fun onChatSelected(chatId: Int) {
		store.accept(MessengerStore.Intent.onChatSelected(chatId))
	}

	@Serializable
	sealed interface Config {
		@Serializable data object ChatsList: Config
		@Serializable data class Chat(val chatId: Int): Config
	}
}