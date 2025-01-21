package com.kaelesty.madprojects.data

import com.kaelesty.madprojects.data.local.localModule
import com.kaelesty.madprojects.data.remote.remoteModule
import com.kaelesty.madprojects.data.features.reposModule
import org.koin.dsl.module

val dataModule = module {
    includes(remoteModule, reposModule, localModule)
}