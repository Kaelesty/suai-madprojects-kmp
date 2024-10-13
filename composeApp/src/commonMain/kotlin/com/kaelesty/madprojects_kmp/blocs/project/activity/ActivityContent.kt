package com.kaelesty.madprojects_kmp.blocs.project.activity

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard

@Composable
fun ActivityContent(
	component: ActivityComponent
) {

	val state by component.state.collectAsState()

	val globalScrollState = rememberScrollState()
	val activityScrollState = rememberScrollState()
	val sprintScrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.scrollable(
				globalScrollState,
				orientation = Orientation.Vertical
			)
	) {
		TitledRoundedCard(
			"Последняя активность",
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 4.dp)
				.height(300.dp)
				.scrollable(
					activityScrollState,
					orientation = Orientation.Vertical
				)
		) {
			state.activityList.forEach {
				ActivityEvent(it)
			}
		}
		Spacer(Modifier.height(8.dp))
		TitledRoundedCard(
			"Спринты",
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 4.dp)
				.height(300.dp)
				.scrollable(
					sprintScrollState,
					orientation = Orientation.Vertical
				)
		) {
			state.sprintList.forEach {
				Sprint(it)
			}
		}
	}
}

@Composable
fun ActivityEvent(
	event: ActivityStore.State.ActivityEvent
) {

}

@Composable
fun Sprint(
	sprint: ActivityStore.State.Sprint
) {

}