package com.kaelesty.madprojects_kmp.blocs.project.kanban

import com.arkivanov.decompose.ComponentContext

class DefaultKanbanComponent(
	private val componentContext: ComponentContext
): ComponentContext by componentContext, KanbanComponent {
}