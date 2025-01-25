package com.kaelesty.madprojects.view.components.main.project.sprint

import com.arkivanov.decompose.ComponentContext

interface SprintComponent {

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,sprintId: String
        ): SprintComponent
    }
}

class DefaultSprintComponent(
    private val componentContext: ComponentContext,
    private val navigator: SprintComponent.Navigator,
    private val projectId: String,
    private val sprintId: String
): ComponentContext by componentContext, SprintComponent {
}