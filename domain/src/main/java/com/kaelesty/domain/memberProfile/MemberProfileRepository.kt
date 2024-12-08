package com.kaelesty.domain.memberProfile

import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.memberProfile.getProfile.GetProfileBody

interface MemberProfileRepository {

    suspend fun getAvatarUrl(jwt: String): String?

    suspend fun getProjects(jwt: String): List<GetProfileBody.ProjectBody>
}