package com.kaelesty.madprojects.view.components.main.project.kanban

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent

interface KanbanComponent {

    interface Navigator

    interface Factory {
        fun create(
            c: ComponentContext, n: Navigator,
            projectId: String,
        ): KanbanComponent
    }
}

class DefaultKanbanComponent(
    private val componentContext: ComponentContext,
    private val navigator: KanbanComponent.Navigator,
    projectId: String,
): ComponentContext by componentContext, KanbanComponent {
}