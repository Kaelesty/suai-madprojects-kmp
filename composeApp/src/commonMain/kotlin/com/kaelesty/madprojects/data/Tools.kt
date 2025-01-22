package com.kaelesty.madprojects.data

import com.kaelesty.madprojects.data.features.auth.LoginManager
import kotlinx.datetime.Clock

object UnauthorizedException: Throwable()

suspend fun <T> T.suspended(
    block: suspend () -> Unit
): T {
    block()
    return this
}

fun Long.isExpired() = Clock.System.now().toEpochMilliseconds() >= this