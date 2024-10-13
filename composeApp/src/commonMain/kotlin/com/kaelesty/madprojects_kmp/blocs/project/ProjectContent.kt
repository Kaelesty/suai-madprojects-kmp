package com.kaelesty.madprojects_kmp.blocs.project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
			.fillMaxSize(),
		topBar = { ProjectTopBar(component) },
		bottomBar = {
			ProjectBottomBar(
				component,
				navTarget
			)
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
	val gradientColors = listOf(
		MaterialTheme.colors.secondary,
		MaterialTheme.colors.secondaryVariant,
		MaterialTheme.colors.onSecondary
	)
	Box(
		modifier = Modifier.fillMaxWidth()
	) {
		Surface(
			modifier = Modifier
				.background(
					color = Color.White
				)
				.fillMaxWidth()
				.height(60.dp)
				.bottomBorder(
					brush = Brush.linearGradient(gradientColors, tileMode = TileMode.Decal),
					height = 8f
				)
		) { }
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
		) {
			Spacer(Modifier.width(16.dp))
			Text(
				text = "mp",
				style = MaterialTheme.typography.caption,
				fontSize = 80.sp,
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = "Проекты/${state.projectName}",
				style = MaterialTheme.typography.body2
					.copy(
						fontSize = 30.sp,
						fontWeight = FontWeight.ExtraLight
					),
				modifier = Modifier
					.offset(y= (- 4).dp)
			)
		}
	}
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