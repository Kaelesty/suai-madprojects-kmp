package com.kaelesty.madprojects.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient(CIO) {
    
    install(HttpTimeout) {
        requestTimeoutMillis = 1000L * 60L
        connectTimeoutMillis = 1000L * 30L
        socketTimeoutMillis = 1000L * 60L
    }

    install(ContentNegotiation) {
        json()
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
        level = LogLevel.ALL
    }
}