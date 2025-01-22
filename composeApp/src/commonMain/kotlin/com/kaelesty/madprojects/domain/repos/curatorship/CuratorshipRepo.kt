package com.kaelesty.madprojects.domain.repos.curatorship

import com.kaelesty.madprojects.domain.repos.project.ProjectGroup

interface CuratorshipRepo {

    suspend fun getCuratorProjectGroups(curatorId: String): Result<List<ProjectGroup>>

    suspend fun getCuratorsList(): Result<List<AvailableCurator>>
}