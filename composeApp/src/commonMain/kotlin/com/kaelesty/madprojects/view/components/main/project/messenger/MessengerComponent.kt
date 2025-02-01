package com.kaelesty.madprojects_kmp.blocs.project.messenger

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chat.ChatComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.ChatsListComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.DefaultChatListComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf

interface MessengerComponent {

	val stack: Value<ChildStack<*, Child>>

	val state: StateFlow<MessengerStore.State>

	sealed interface Child {

		class ChatsList(val component: ChatsListComponent): Child

		class Chat(val component: ChatComponent): Child
	}

	interface Factory {

		fun create(
			componentContext: ComponentContext,
			projectId: String,
		): MessengerComponent
	}
}

class DefaultMessengerComponent(
	projectId: String,
	private val componentContext: ComponentContext,
	private val storeFactory: MessengerStoreFactory,
	private val chatComponentFactory: ChatComponent.Factory,
	private val chatListComponentFactory: ChatsListComponent.Factory,
	private val authRepo: AuthRepo,
): ComponentContext by componentContext, MessengerComponent {

	private val scope = CoroutineScope(Dispatchers.Main)

	private val navigation = StackNavigation<Config>()

	private val store = instanceKeeper.getStore {
		storeFactory.create(
			userId = authRepo.getUser().value?.id?.toInt() ?: -1,
			projectId = projectId.toInt()
		).apply {
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

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<MessengerStore.State>
		get() = store.stateFlow

	private fun child(
		config: Config,
		componentContext: ComponentContext,
	): MessengerComponent.Child = when (config) {
		is Config.Chat -> MessengerComponent.Child.Chat(
			component = chatComponentFactory.create(
				componentContext, store,
				chatId = config.chatId
			)
		)
		Config.ChatsList -> MessengerComponent.Child.ChatsList(
			component = chatListComponentFactory.create(
				componentContext =componentContext,
				navigator = object : ChatsListComponent.Navigator {

					override fun toChat(chatId: Int) {
						navigation.push(Config.Chat(chatId))
					}

				},
				store = store
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

	@Serializable
	sealed interface Config {
		@Serializable
		data object ChatsList: Config
		@Serializable
		data class Chat(val chatId: Int): Config
	}
}