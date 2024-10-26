package com.kaelesty.domain.memberProfile.getProfile

import com.kaelesty.domain.memberProfile.MemberProfileRepository

class GetMemberProfileUseCase(
    private val repo: MemberProfileRepository
) {

    suspend operator fun invoke(jwt: String) = repo.getMemberProfile(jwt)
}