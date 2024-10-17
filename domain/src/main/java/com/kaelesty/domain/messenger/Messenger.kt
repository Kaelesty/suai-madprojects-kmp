package com.kaelesty.domain.messenger

import entities.ClientAction
import entities.ServerAction
import kotlinx.coroutines.flow.SharedFlow

interface Messenger {

	suspend fun connect(onFinish: () -> Unit)

	val actionsFlow: SharedFlow<ServerAction>

	suspend fun acceptAction(action: ClientAction)
}