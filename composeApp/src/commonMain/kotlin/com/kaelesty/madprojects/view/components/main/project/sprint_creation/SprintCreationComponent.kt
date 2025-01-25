package com.kaelesty.madprojects.view.components.main.project.sprint_creation

import com.arkivanov.decompose.ComponentContext

interface SprintCreationComponent {

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): SprintCreationComponent
    }
}

class DefaultSprintCreationComponent(
    private val componentContext: ComponentContext,
    private val navigator: SprintCreationComponent.Navigator,
    private val projectId: String,
): ComponentContext by componentContext, SprintCreationComponent {
}