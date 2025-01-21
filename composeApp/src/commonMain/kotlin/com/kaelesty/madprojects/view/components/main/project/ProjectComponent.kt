package com.kaelesty.madprojects.view.components.main.project

import com.arkivanov.decompose.ComponentContext

interface ProjectComponent {

    interface Navigator {
        // todo
    }

    interface Factory {

        fun create(
            c: ComponentContext,
            n: Navigator,
            projectId: String
        ): ProjectComponent
    }
}

class DefaultProjectComponent(
    private val componentContext: ComponentContext,
    private val navigator: ProjectComponent.Navigator,
    private val projectId: String
): ProjectComponent, ComponentContext by componentContext