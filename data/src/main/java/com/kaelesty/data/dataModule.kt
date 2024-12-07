package com.kaelesty.data

import com.kaelesty.domain.messenger.Socket
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val dataModule = module {
    single<Socket> {
        SocketImpl()
    }

    single<Ktorfit> {
        Ktorfit.Builder()
            .baseUrl("")
    }
}