package com.kaelesty.madprojects_kmp.blocs.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityContent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoContent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanContent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerContent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsContent
import com.kaelesty.madprojects_kmp.ui.uikit.cards.NavBarCard

@Composable
fun ProjectContent(
	component: ProjectComponent
) {
	val currentChild by component.stack.subscribeAsState()
	val navTarget = ProjectComponent.ChildToNavTarget(currentChild.active.instance)

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
		,
		topBar = { Text("AbobaTobBar") },
		bottomBar = { ProjectBottomBar(
			component,
			navTarget
		) }
	) { paddingValues ->
		Children(
			stack = component.stack,
			modifier = Modifier.padding(paddingValues)
		) {
			when (val instance = it.instance) {
				is ProjectComponent.Child.Activity -> ActivityContent(component = instance.component)
				is ProjectComponent.Child.Info -> InfoContent(component = instance.component)
				is ProjectComponent.Child.Kanban -> KanbanContent(component = instance.component)
				is ProjectComponent.Child.Messenger -> MessengerContent(component = instance.component)
				is ProjectComponent.Child.Settings -> SettingsContent(component = instance.component)
			}
		}
	}

}

@Composable
fun ProjectBottomBar(
	component: ProjectComponent,
	selected: ProjectComponent.Child.NavTarget,

) {
	val children = ProjectComponent.Child.NavTarget.entries

	Row(
		modifier = Modifier
			.padding(horizontal = 4.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		children.forEach {
			NavBarCard(
				isSelected = it == selected,
				navTarget = it,
				modifier = Modifier
					.weight(1f / children.size)
				,
				onClick = {
					component.navigate(it)
				}
			)
		}
	}
}