package com.kaelesty.domain.memberProfile

import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.memberProfile.getProfile.GetProfileBody
import com.kaelesty.domain.memberProfile.getProfile.GetProfileErrors

interface MemberProfileRepository {

    suspend fun getMemberProfile(jwt: String): UseCaseResult<GetProfileBody, GetProfileErrors>
}