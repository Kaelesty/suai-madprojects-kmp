package com.kaelesty.data

import com.kaelesty.domain.messenger.Socket
import entities.Intent
import entities.Action
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocketImpl : Socket {

	private val scope = CoroutineScope(Dispatchers.Default)

	private val client = HttpClient(CIO) {
		install(WebSockets)
	}
	private var websocketSession: WebSocketSession? = null

	private val outgoingFlow = MutableSharedFlow<Intent>()
	override suspend fun connect(
		onFinish: () -> Unit
	) {
		websocketSession = client.webSocketSession("ws://kaelesty.ru:8080/project").also { session ->
			scope.launch {
				outgoingFlow.collect {
					session.send(Frame.Text(Json.encodeToString(it)))
				}
			}
			onFinish()
			session.incoming.consumeEach {
				if (it is Frame.Text) {
					_actionsFlow.emit(
						Json.decodeFromString<Action>(it.readText())
					)
				}
			}
		}
	}

	private val _actionsFlow = MutableSharedFlow<Action>()
	override val actionsFlow: SharedFlow<Action>
		get() = _actionsFlow.asSharedFlow()

	override suspend fun acceptIntent(intent: Intent) {
		outgoingFlow.emit(intent)
	}
}