package com.kaelesty.domain.common

interface JwtTool {

    fun getJwt(): String

    fun saveJwt(jwt: String)

    fun notifyJwtExpired()
}