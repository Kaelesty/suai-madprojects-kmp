package com.kaelesty.domain.di

import org.koin.dsl.module

val domainModule = module {
	includes(authModule)
}