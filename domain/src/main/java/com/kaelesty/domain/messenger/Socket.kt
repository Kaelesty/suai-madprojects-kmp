package com.kaelesty.domain.messenger

import entities.Intent
import entities.Action
import kotlinx.coroutines.flow.SharedFlow

interface Socket {

	suspend fun connect(onFinish: () -> Unit)

	val actionsFlow: SharedFlow<Action>


	suspend fun acceptIntent(intent: Intent)
}