package com.kaelesty.domain.di

import com.kaelesty.domain.memberProfile.getProfile.GetMemberProfileUseCase
import org.koin.dsl.module

val memberProfileModule = module {
    factory<GetMemberProfileUseCase> {
        GetMemberProfileUseCase(
            repo = get(),
            authenticationManager = get()
        )
    }
}