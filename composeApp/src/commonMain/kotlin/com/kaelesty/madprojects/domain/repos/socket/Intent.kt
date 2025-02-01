package com.kaelesty.madprojects.domain.repos.socket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Intent")
sealed interface Intent {

    @Serializable
    @SerialName("KeepAlive")
    object KeepAlive: Intent

    @Serializable
    @SerialName("Authorize") class Authorize(
        @SerialName("jwt") val jwt: String,
    ): Intent

    @Serializable
    @SerialName("CloseSession")
    data object CloseSession : Intent

    @Serializable
    @SerialName("Messenger")
    sealed interface Messenger: Intent {

        @Serializable
        @SerialName("Start")
        data class Start(
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("Stop")
        data class Stop(
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("SendMessage")
        class SendMessage(
            @SerialName("chatId") val chatId: Int,
            @SerialName("message") val message: String,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("CreateChat")
        class CreateChat(
            @SerialName("projectId") val projectId: Int,
            @SerialName("chatTitle") val chatTitle: String,
            @SerialName("chatType") val chatType: ChatType,
        ): Messenger

        @Serializable
        @SerialName("RequestChatMessages")
        class RequestChatMessages(
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("RequestChatMessages")
        class RequestChatsList(
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("ReadMessage")
        class ReadMessage(
            @SerialName("messageId") val messageId: Int,
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger

        @Serializable
        @SerialName("ReadMessagesBefore")
        class ReadMessagesBefore(
            @SerialName("messageId") val messageId: Int,
            @SerialName("chatId") val chatId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Messenger
    }

    @Serializable
    @SerialName("Kanban")
    sealed interface Kanban: Intent {

        @Serializable
        @SerialName("Start")
        data class Start(
            @SerialName("ProjectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("Stop")
        data class Stop(
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("GetKanban")
        data class GetKanban(
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("CreateKard")
        data class CreateKard(
            @SerialName("name") val name: String,
            @SerialName("desc") val desc: String,
            @SerialName("columnId") val columnId: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("MoveKard")
        data class MoveKard(
            @SerialName("id") val id: Int,
            @SerialName("columnId") val columnId: Int,
            @SerialName("newColumnId") val newColumnId: Int,
            @SerialName("newPosition") val newPosition: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("CreateColumn")
        data class CreateColumn(
            @SerialName("name") val name: String,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("MoveColumn")
        data class MoveColumn(
            @SerialName("id") val id: Int,
            @SerialName("newPosition") val newPosition: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("UpdateKard")
        data class UpdateKard(
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String?,
            @SerialName("desc") val desc: String?,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("UpdateColumn")
        data class UpdateColumn(
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String?,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("DeleteKard")
        data class DeleteKard(
            @SerialName("id") val id: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban

        @Serializable
        @SerialName("DeleteColumn")
        data class DeleteColumn(
            @SerialName("id") val id: Int,
            @SerialName("projectId") val projectId: Int,
        ): Kanban
    }
}