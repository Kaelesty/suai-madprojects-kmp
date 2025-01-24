package com.kaelesty.madprojects.view.components.main.project.messenger

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent

interface MessengerComponent {

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): MessengerComponent
    }
}

class DefaultMessengerComponent(
    private val componentContext: ComponentContext,
    private val navigator: MessengerComponent.Navigator,
    projectId: String,
): ComponentContext by componentContext, MessengerComponent {
}
