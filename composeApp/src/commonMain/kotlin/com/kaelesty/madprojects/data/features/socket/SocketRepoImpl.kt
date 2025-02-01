package com.kaelesty.madprojects.data.features.socket

import com.kaelesty.madprojects.domain.repos.socket.Action
import com.kaelesty.madprojects.domain.repos.socket.Intent
import com.kaelesty.madprojects.domain.repos.socket.SocketRepository
import kotlinx.coroutines.flow.SharedFlow

class SocketRepoImpl: SocketRepository {

    override val action: SharedFlow<Action>
        get() = TODO("Not yet implemented")

    override suspend fun accept(intent: Intent) {
        TODO("Not yet implemented")
    }

    override suspend fun start() {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }
}