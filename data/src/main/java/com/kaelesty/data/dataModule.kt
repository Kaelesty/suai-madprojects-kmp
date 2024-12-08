package com.kaelesty.data

import com.kaelesty.data.auth.authModule
import com.kaelesty.data.profile.memberProfileModule
import com.kaelesty.domain.messenger.Socket
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {

    includes(
        authModule,
        memberProfileModule,
    )

    single<Socket> {
        SocketImpl()
    }

    single<Ktorfit> {
        Ktorfit.Builder()
            .baseUrl("https://kaelesty.ru:5000/api/")
            .build()
    }

    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}