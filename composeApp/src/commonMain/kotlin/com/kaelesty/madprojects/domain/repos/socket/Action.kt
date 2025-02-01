package com.kaelesty.madprojects.domain.repos.socket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Action")
sealed interface Action {

    @Serializable
    @SerialName("KeepAlive")
    object KeepAlive: Action

    @Serializable
    @SerialName("Unauthorized")
    object Unauthorized: Action

    @Serializable
    @SerialName("Messenger")
    sealed interface Messenger: Action {

        @Serializable
        @SerialName("NewMessage")
        class NewMessage(
            @SerialName("chatId") val chatId: Int,
            @SerialName("message") val message: Message,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("SendChatsList")
        class SendChatsList(
            @SerialName("chats") val chats: List<Chat>,
            @SerialName("projectId") val projectId: Int,
            @SerialName("senders") val senders: List<ChatSender>
        ): Messenger

        @Serializable
        @SerialName("NewChat")
        class NewChat(
            @SerialName("chat") val chat: Chat,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("SendChatMessages")
        class SendChatMessages(
            @SerialName("chatId") val chatId: Int,
            @SerialName("readMessages") val readMessages: List<Message>,
            @SerialName("unreadMessages") val unreadMessages: List<Message>,
            @SerialName("projectId") val projectId: Int,
            @SerialName("userId") val userId: String,
        ): Messenger


        @Serializable
        @SerialName("MessageReadRecorded")
        class MessageReadRecorded(
            @SerialName("messageId") val messageId: Int,
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectID") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("UpdateChatUnreadCount")
        class UpdateChatUnreadCount(
            @SerialName("chatId") val chatId: Int,
            @SerialName("count") val count: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger
    }

    @Serializable
    @SerialName("Kanban")
    sealed interface Kanban: Action {

        @Serializable
        @SerialName("SetState")
        data class SetState(
            @SerialName("kanban") val kanban: KanbanState,
            @SerialName("projectId") val projectId: Int,
        ): Kanban
    }
}