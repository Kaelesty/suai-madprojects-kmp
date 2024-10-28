package com.kaelesty.madprojects_kmp.blocs.project.kanban

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import entities.KanbanState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultKanbanComponent(
	private val componentContext: ComponentContext,
	private val storeFactory: KanbanStoreFactory,
): ComponentContext by componentContext, KanbanComponent {

	private val scope = CoroutineScope(Dispatchers.Main)

	private val store = instanceKeeper.getStore {
		storeFactory.create().apply {
			scope.launch {
				labels.collect {
					// TODO
				}
			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<KanbanStore.State> = store.stateFlow

	override fun createColumn(name: String) {
		store.accept(KanbanStore.Intent.CreateColumn(name))
	}

	override fun createKard(name: String, desc: String, columnId: Int) {
		store.accept(KanbanStore.Intent.CreateKard(
			columnId = columnId,
			desc = desc,
			title = name,
		))
	}

	override fun moveKard(id: Int, columnId: Int, newColumnId: Int, newOrder: Int) {
		store.accept(KanbanStore.Intent.MoveKard(
			kardId = id,
			columnId = columnId,
			newColumnId = newColumnId,
			newOrder = newOrder,
		))
	}

	override fun moveColumn(id: Int, newOrder: Int) {
		store.accept(KanbanStore.Intent.MoveColumn(
			columnId = id,
			newOrder = newOrder
		))
	}

	override fun updateKard(kard: KanbanState.Kard, newName: String, newDesc: String) {
		store.accept(KanbanStore.Intent.UpdateKard(
			kard,
			newName, newDesc
		))
	}

	override fun deleteKard(id: Int) {
		TODO("Not yet implemented")
	}

	override fun deleteColumn(id: Int) {
		TODO("Not yet implemented")
	}
}