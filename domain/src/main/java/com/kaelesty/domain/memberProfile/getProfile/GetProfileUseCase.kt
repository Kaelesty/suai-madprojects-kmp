package com.kaelesty.domain.memberProfile.getProfile

import com.kaelesty.domain.auth.AuthenticationManager
import com.kaelesty.domain.memberProfile.MemberProfileRepository

class GetMemberProfileUseCase(
    private val repo: MemberProfileRepository,
    private val authenticationManager: AuthenticationManager,
) {

    suspend operator fun invoke(): GetProfileBody {
        val authenticationContext = authenticationManager.getContext()

        val avatar = repo.getAvatarUrl(
            jwt = authenticationContext?.token ?: ""
        )

        return GetProfileBody(
            userName = authenticationContext?.username ?: "...",
            avatarUrl = avatar ?: "https://samsunggamer.hu/uploads/page_texts/sq/0000/032/Among-us-party-32-1605633457.png",
            group = authenticationContext?.group ?: "...",
            email = authenticationContext?.email ?: "...",
            projects = repo.getProjects(authenticationContext?.token ?: ""),
            firstName = authenticationContext?.firstName ?: "...",
            secondName = authenticationContext?.secondName ?: "...",
            lastName = authenticationContext?.lastName ?: "...",

            )
    }
}