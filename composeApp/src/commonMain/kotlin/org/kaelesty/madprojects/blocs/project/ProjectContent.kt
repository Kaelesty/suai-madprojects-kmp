package com.kaelesty.madprojects_kmp.blocs.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityContent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoContent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanContent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerContent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsContent
import com.kaelesty.madprojects_kmp.ui.uikit.cards.NavBarCard
import com.kaelesty.madprojects_kmp.ui.uikit.layout.TopBar

@Composable
fun ProjectContent(
	component: ProjectComponent
) {
	val currentChild by component.stack.subscribeAsState()
	val navTarget = ProjectComponent.ChildToNavTarget(currentChild.active.instance)

	var showBottomBar by rememberSaveable {
		mutableStateOf(true)
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize(),
		topBar = { ProjectTopBar(component) },
		bottomBar = {
			if (showBottomBar) {
				ProjectBottomBar(
					component,
					navTarget
				)
			}
		}
	) { paddingValues ->
		Children(
			stack = component.stack,
			modifier = Modifier.padding(paddingValues)
		) {
			when (val instance = it.instance) {
				is ProjectComponent.Child.Activity -> ActivityContent(component = instance.component)
				is ProjectComponent.Child.Info -> InfoContent(component = instance.component)
				is ProjectComponent.Child.Kanban -> KanbanContent(component = instance.component)
				is ProjectComponent.Child.Messenger -> MessengerContent(
					component = instance.component,
					onChatShown = {
						showBottomBar = it
					}
				)
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
					.weight(1f / children.size),
				onClick = {
					component.navigate(it)
				}
			)
		}
	}
}

@Composable
fun ProjectTopBar(
	component: ProjectComponent
) {
	val state by component.state.collectAsState()
	TopBar("Проекты/${state.projectName ?: "Project"} ")
}

fun Modifier.bottomBorder(
    brush: Brush,
    height: Float,
) = this.drawWithContent {
	drawContent()
	drawLine(
		brush = brush,
		start = Offset(0f, size.height),
		end = Offset(size.width, size.height),
		strokeWidth = height,
	)
}