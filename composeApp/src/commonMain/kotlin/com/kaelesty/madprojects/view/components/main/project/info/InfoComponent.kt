package com.kaelesty.madprojects_kmp.blocs.project.info

import com.arkivanov.decompose.ComponentContext

interface InfoComponent {

    interface Navigator

    interface Factory {

        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String
        ): InfoComponent
    }
}

class DefaultInfoComponent(
    private val componentContext: ComponentContext,
    private val navigator: InfoComponent.Navigator,
    projectId: String,
): ComponentContext by componentContext, InfoComponent {
}