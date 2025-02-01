package com.kaelesty.madprojects.data.features.socket

import com.kaelesty.madprojects.data.BASE_URL_SOCKET
import com.kaelesty.madprojects.data.features.auth.LoginManager
import com.kaelesty.madprojects.domain.repos.socket.Action
import com.kaelesty.madprojects.domain.repos.socket.Intent
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocketRepoImpl(
    private val loginManager: LoginManager
): SocketRepository {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var websocketSession: WebSocketSession? = null

    private val outgoingFlow = MutableSharedFlow<Intent>()

    private val _actionsFlow = MutableSharedFlow<Action>(replay = 5)
    override val action: SharedFlow<Action>
        get() = _actionsFlow.asSharedFlow()

    override suspend fun accept(intent: Intent) {
        outgoingFlow.emit(intent as Intent)
    }

    override suspend fun start(onConnected: suspend () -> Unit) {
        try {
            val token = loginManager.getTokenOrThrow()
            websocketSession = client.webSocketSession(BASE_URL_SOCKET).also { session ->
                scope.launch {
                    outgoingFlow.collect {
                        val json = Json.encodeToString(it as Intent)
                        session.send(Frame.Text(json))
                    }
                }
                val json = Json.encodeToString(Intent.Authorize(token) as Intent)
                session.send(
                    Frame.Text(
                        json
                    )
                )
                onConnected()
                session.incoming.consumeEach {
                    if (it is Frame.Text) {
                        _actionsFlow.emit(
                            Json.decodeFromString<Action>(it.readText())
                        )
                    }
                }
            }
        }
        catch (e: Exception) {
            e.toString()
        }
    }

    override suspend fun stop() {
        websocketSession?.close()
        websocketSession = null
        scope.coroutineContext.cancelChildren()
    }
}