package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.coroutines.flow.SharedFlow

interface SocketRepository {

    val action: SharedFlow<Action>

    suspend fun accept(intent: Intent)

    suspend fun start(onConnected: suspend () -> Unit)

    suspend fun stop()
}