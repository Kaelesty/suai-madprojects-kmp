package com.kaelesty.domain.di

import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.memberProfile.MemberProfileRepository
import com.kaelesty.domain.memberProfile.getProfile.GetMemberProfileUseCase
import com.kaelesty.domain.memberProfile.getProfile.GetProfileBody
import com.kaelesty.domain.memberProfile.getProfile.GetProfileErrors
import org.koin.dsl.module

val memberProfileModule = module {
    factory<GetMemberProfileUseCase> {
        GetMemberProfileUseCase(
            repo = get()
        )
    }

    single<MemberProfileRepository> {
        object : MemberProfileRepository {
            override suspend fun getMemberProfile(jwt: String): UseCaseResult<GetProfileBody, GetProfileErrors> {
                return UseCaseResult.Success(
                    body = GetProfileBody(
                        userName = "Бунделев Илья Алексеевич",
                        avatarUrl = "https://i.pinimg.com/564x/48/19/a4/4819a47aec8284eb47e4ebfed87443cd.jpg",
                        githubLink = "https://github.com/Kaelesty",
                        email = "kaelesty@email.com",
                        group = "4215",
                        projects = listOf(
                            GetProfileBody.ProjectBody(
                                id = 1,
                                name = "Audionautica"
                            )
                        )
                    )
                )
            }
        }
    }
}