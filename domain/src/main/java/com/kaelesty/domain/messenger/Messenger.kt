package com.kaelesty.domain.messenger

import entities.ClientAction
import entities.ServerAction
import kotlinx.coroutines.flow.SharedFlow

interface Messenger {

	suspend fun connect()

	val actionsFlow: SharedFlow<ServerAction>

	fun acceptAction(action: ClientAction)
}