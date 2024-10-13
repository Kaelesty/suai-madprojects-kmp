package com.kaelesty.madprojects_kmp.blocs.project.activity

import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityStore.State
import kotlinx.coroutines.flow.StateFlow

interface ActivityComponent {

	val state: StateFlow<State>

	fun openActivityLink(
		linkTargetId: Int,
		linkTargetType: State.ActivityEvent.LinkTargetType,
	)

	fun openSprintScreen(
		sprintId: Int,
	)
}