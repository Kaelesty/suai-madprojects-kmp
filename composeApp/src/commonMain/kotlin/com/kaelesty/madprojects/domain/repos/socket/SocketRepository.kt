package com.kaelesty.madprojects.domain.repos.socket

import kotlinx.coroutines.flow.SharedFlow

interface SocketRepository {

    val action: SharedFlow<Action>

    suspend fun accept(intent: Intent)

    suspend fun start()

    suspend fun stop()
}