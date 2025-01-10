package com.kaelesty.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol

val httpClient = HttpClient(CIO) {
    defaultRequest {
        host = "mad-projects.ru:8080"
        url {
            protocol = URLProtocol.HTTPS
        }
    }
}