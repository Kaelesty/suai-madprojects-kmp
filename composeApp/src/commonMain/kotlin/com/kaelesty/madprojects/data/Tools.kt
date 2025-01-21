package com.kaelesty.madprojects.data

import com.kaelesty.madprojects.data.features.auth.LoginManager

object UnauthorizedException: Throwable()

suspend fun <T> T.suspended(
    block: suspend () -> Unit
): T {
    block()
    return this
}