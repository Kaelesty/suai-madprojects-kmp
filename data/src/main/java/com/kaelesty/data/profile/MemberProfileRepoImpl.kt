package com.kaelesty.data.profile

import com.kaelesty.domain.common.UseCaseResult
import com.kaelesty.domain.memberProfile.MemberProfileRepository
import com.kaelesty.domain.memberProfile.getProfile.GetProfileBody

class MemberProfileRepoImpl: MemberProfileRepository {


    override suspend fun getAvatarUrl(jwt: String): String? {
        // TODO
        return null
    }

    override suspend fun getProjects(jwt: String): List<GetProfileBody.ProjectBody> {
        // TODO
        return listOf()
    }
}