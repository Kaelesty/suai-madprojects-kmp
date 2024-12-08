package com.kaelesty.domain.projects

interface ProjectsRepository {

    suspend fun createProject(
        jwt: String,
        name: String,
        desc: String,
        date: String,
        numParticipants: Int,
        approved: Boolean,
        inviteCode: String,
    )

}