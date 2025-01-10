package com.kaelesty.madprojects_kmp.blocs.project.kanban

import entities.KanbanState
import kotlinx.coroutines.flow.StateFlow

interface KanbanComponent {

    val state: StateFlow<KanbanStore.State>

    fun createColumn(name: String)

    fun createKard(name: String, desc: String, columnId: Int)

    fun moveKard(id: Int, columnId: Int, newColumnId: Int, newOrder: Int)

    fun moveColumn(id: Int, newOrder: Int)

    fun updateKard(kard: KanbanState.Kard, newName: String, newDesc: String)

    fun deleteKard(id: Int)

    fun deleteColumn(id: Int)
}