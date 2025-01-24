package com.kaelesty.madprojects_kmp.blocs.project.settings

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent.Navigator

interface SettingsComponent {

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String
        ): SettingsComponent
    }
}

class DefaultSettingsComponent(
    private val componentContext: ComponentContext,
    private val navigator: SettingsComponent.Navigator,
    projectId: String,
): ComponentContext by componentContext, SettingsComponent {
}