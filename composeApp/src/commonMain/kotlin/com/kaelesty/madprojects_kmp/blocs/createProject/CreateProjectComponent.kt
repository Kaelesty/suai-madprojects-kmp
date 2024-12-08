package com.kaelesty.madprojects_kmp.blocs.createProject

import kotlinx.coroutines.flow.StateFlow

interface CreateProjectComponent {

    val state: StateFlow<CreateProjectStore.State>

    fun accept(intent: CreateProjectStore.Intent)
}