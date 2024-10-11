package com.kaelesty.madprojects_kmp.blocs.project

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
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityContent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoContent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanContent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerContent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsContent

@Composable
fun ProjectContent(
	component: ProjectComponent
) {
	val currentChild by component.stack.subscribeAsState()
	val navTarget = ProjectComponent.ChildToNavTarget(currentChild.active.instance)

	Scaffold(
		modifier = Modifier.fillMaxSize(),
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
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceAround
	) {
		children.forEach {
			Text(
				it.toString(),
				fontWeight = if (it == selected) FontWeight.Bold else FontWeight.Thin,
				modifier = Modifier
					.clickable {
						component.navigate(it)
					}
			)
		}
	}
}