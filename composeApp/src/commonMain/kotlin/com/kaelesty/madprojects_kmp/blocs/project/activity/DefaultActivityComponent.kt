package com.kaelesty.madprojects_kmp.blocs.project.activity

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultActivityComponent(
	private val componentContext: ComponentContext,
	private val storeFactory: ActivityStoreFactory,
): ComponentContext by componentContext, ActivityComponent {

	private val store = instanceKeeper.getStore {
		storeFactory.create()
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val state: StateFlow<ActivityStore.State>
		get() = store.stateFlow

	override fun openActivityLink(
		linkTargetId: Int,
		linkTargetType: ActivityStore.State.ActivityEvent.LinkTargetType
	) {
		store.accept(ActivityStore.Intent.OpenActivityLink(linkTargetId, linkTargetType))
	}

	override fun openSprintScreen(sprintId: Int) {
		store.accept(ActivityStore.Intent.OpenSprint(sprintId))
	}

	private fun handleLabel(label: ActivityStore.Label) {
		when (label) {
			else -> {}
		}
	}
}