package com.kaelesty.madprojects.view.components.main.project.activity

import com.arkivanov.decompose.ComponentContext

interface ActivityComponent {

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): ActivityComponent
    }
}

class DefaultActivityComponent(
    private val componentContext: ComponentContext,
    private val navigator: ActivityComponent.Navigator,
    projectId: String,
): ComponentContext by componentContext, ActivityComponent {
}