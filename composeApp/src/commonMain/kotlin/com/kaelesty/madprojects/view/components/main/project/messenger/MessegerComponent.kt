package com.kaelesty.madprojects.view.components.main.project.messenger

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.socket.Chat
import com.kaelesty.madprojects.domain.repos.socket.Message
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import kotlinx.coroutines.flow.StateFlow

interface MessengerComponent {

    data class State(
        val chats: List<ChatState>
    ) {
        data class ChatState(
            val chat: Chat,
            val readMessages: List<Message>,
            val unreadMessages: List<Message>,
        )
    }

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): MessengerComponent
    }

    sealed interface Child {

        class ChatsList(val component: ChatsListComponent): Child

        class Chat(val component: ChatComponent): Child
    }

    val state: StateFlow<State>
}

class DefaultMessengerComponent(
    private val componentContext: ComponentContext,
    private val navigator: MessengerComponent.Navigator,
    projectId: String,
    private val socketRepository: SocketRepository,
): ComponentContext by componentContext, MessengerComponent {
}
