package entities

import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientAction {

    @Serializable
    class Authorize(
        val jwt: String
    ): ClientAction

    @Serializable
    class SendMessage(
        val chatId: Int,
        val message: String
    ): ClientAction

    @Serializable
    class CreateChat(
        val projectId: Int,
        val chatTitle: String,
        val chatType: ChatType
    ): ClientAction

    @Serializable
    class RequestChatMessages(
        val chatId: Int,
    ): ClientAction

    @Serializable
    class RequestChatsList(
        val projectId: Int,
    ): ClientAction
}