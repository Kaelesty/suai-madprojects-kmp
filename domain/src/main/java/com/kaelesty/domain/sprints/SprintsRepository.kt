package com.kaelesty.domain.sprints

import com.kaelesty.domain.common.UseCaseResult

interface SprintsRepository {

    suspend fun getProjectSprints(projectId: Int): List<Sprint>?


}