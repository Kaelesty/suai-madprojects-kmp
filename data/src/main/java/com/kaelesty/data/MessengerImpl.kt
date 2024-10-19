package com.kaelesty.data

import com.kaelesty.domain.messenger.Messenger
import entities.ClientAction
import entities.ServerAction
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessengerImpl : Messenger {

	private val scope = CoroutineScope(Dispatchers.Default)

	private val client = HttpClient(CIO) {
		install(WebSockets)
	}
	private var websocketSession: WebSocketSession? = null

	private val outgoingFlow = MutableSharedFlow<ClientAction>()

	override suspend fun connect(
		onFinish: () -> Unit
	) {
		websocketSession = client.webSocketSession("ws://kaelesty.ru:8080/messenger").also { session ->
			scope.launch {
				println("!")
				outgoingFlow.collect {
					session.send(Frame.Text(Json.encodeToString(it)))
				}
			}
			onFinish()
			session.incoming.consumeEach {
				if (it is Frame.Text) {
					_actionsFlow.emit(
						Json.decodeFromString<ServerAction>(it.readText())
					)
				}
			}
		}
	}

	private val _actionsFlow = MutableSharedFlow<ServerAction>()
	override val actionsFlow: SharedFlow<ServerAction>
		get() = _actionsFlow.asSharedFlow()

	override suspend fun acceptAction(action: ClientAction) {
		outgoingFlow.emit(action)
	}
}