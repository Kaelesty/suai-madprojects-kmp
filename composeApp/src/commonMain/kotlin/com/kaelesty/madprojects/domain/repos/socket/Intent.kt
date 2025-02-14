package com.kaelesty.madprojects.domain.repos.socket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("entities.Intent")
sealed interface Intent {

    @Serializable
    @SerialName("entities.Intent.KeepAlive")
    object KeepAlive: Intent

    @Serializable
    @SerialName("entities.Intent.Authorize") class Authorize(
        @SerialName("jwt") val jwt: String,
    ): Intent

    @Serializable
    @SerialName("entities.Intent.CloseSession")
    data object CloseSession : Intent

    @Serializable
    @SerialName("entities.Intent.Messenger")
    sealed interface Messenger: Intent {

        @Serializable
        @SerialName("entities.Intent.Messenger.Start")
        data class Start(
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.Stop")
        data class Stop(
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.SendMessage")
        class SendMessage(
            @SerialName("chatId") val chatId: Int,
            @SerialName("message") val message: String,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.CreateChat")
        class CreateChat(
            @SerialName("projectId") val projectId: Int,
            @SerialName("chatTitle") val chatTitle: String,
            @SerialName("chatType") val chatType: ChatType,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.RequestChatMessages")
        class RequestChatMessages(
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.RequestChatsList")
        class RequestChatsList(
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.ReadMessage")
        class ReadMessage(
            @SerialName("messageId") val messageId: Int,
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("entities.Intent.Messenger.ReadMessagesBefore")
        class ReadMessagesBefore(
            @SerialName("messageId") val messageId: Int,
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger
    }

    @Serializable
    @SerialName("entities.Intent.Kanban")
    sealed interface Kanban: Intent {

        @Serializable
        @SerialName("entities.Intent.Kanban.Start")
        data class Start(
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.Stop")
        data class Stop(
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.GetKanban")
        data class GetKanban(
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.CreateKard")
        data class CreateKard(
            @SerialName("name") val name: String,
            @SerialName("desc") val desc: String,
            @SerialName("columnId") val columnId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.MoveKard")
        data class MoveKard(
            @SerialName("id") val id: Int,
            @SerialName("columnId") val columnId: Int,
            @SerialName("newColumnId") val newColumnId: Int,
            @SerialName("newPosition") val newPosition: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.CreateColumn")
        data class CreateColumn(
            @SerialName("name") val name: String,
            @SerialName("projectId") val projectId: Int,
            @SerialName("color") val color: String,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.MoveColumn")
        data class MoveColumn(
            @SerialName("id") val id: Int,
            @SerialName("newPosition") val newPosition: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.UpdateKard")
        data class UpdateKard(
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String?,
            @SerialName("desc") val desc: String?,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.UpdateColumn")
        data class UpdateColumn(
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String?,
            @SerialName("projectId") val projectId: Int,
            @SerialName("color") val color: String?,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.DeleteKard")
        data class DeleteKard(
            @SerialName("id") val id: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("entities.Intent.Kanban.DeleteColumn")
        data class DeleteColumn(
            @SerialName("id") val id: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban
    }
}