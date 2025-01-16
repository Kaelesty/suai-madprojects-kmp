package com.kaelesty.madprojects.domain

import com.kaelesty.madprojects.domain.stores.storesModule
import org.koin.dsl.module

val domainModule = module {
    includes(storesModule)
}