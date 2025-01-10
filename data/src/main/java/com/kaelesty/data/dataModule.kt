package com.kaelesty.data

import com.kaelesty.data.remote.remoteModule
import org.koin.dsl.module

val dataModule = module {
    includes(remoteModule)
}