package entities

import kotlinx.serialization.Serializable

@Serializable
sealed interface ServerAction {

    @Serializable
    class NewMessage(
        val chatId: Int,
        val message: Message,
    ): ServerAction

    @Serializable
    class SendChatsList(
        val chats: List<Chat>
    ): ServerAction
    // On connect to WS

    @Serializable
    class NewChat(
        val chat: Chat,
    ): ServerAction

    @Serializable
    class SendChatMessages(
        val chatId: Int,
        val messages: List<Message>
    ): ServerAction
}