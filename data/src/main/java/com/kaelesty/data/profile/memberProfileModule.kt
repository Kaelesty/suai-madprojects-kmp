package com.kaelesty.data.profile

import com.kaelesty.domain.memberProfile.MemberProfileRepository
import org.koin.dsl.module

val memberProfileModule = module {

    single<MemberProfileRepository> {
        MemberProfileRepoImpl()
    }
}