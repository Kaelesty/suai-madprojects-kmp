package com.kaelesty.madprojects.domain.repos.story

interface ActivityRepo {

    suspend fun getProjectActivity(projectId: Int, count: Int?): Result<ActivityResponse>

}