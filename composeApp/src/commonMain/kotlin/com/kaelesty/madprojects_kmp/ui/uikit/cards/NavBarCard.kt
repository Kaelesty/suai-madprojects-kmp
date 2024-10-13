package com.kaelesty.madprojects_kmp.ui.uikit.cards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.blocs.project.ProjectComponent
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.activity_nav
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NavBarCard(
	isSelected: Boolean,
	navTarget: ProjectComponent.Child.NavTarget,
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
) {

	Card(
		modifier = modifier,
		backgroundColor = if (isSelected) MaterialTheme.colors.onPrimary
			else MaterialTheme.colors.surface,
		shape = RoundedCornerShape(
			topStart = 14.dp, topEnd = 14.dp,
			bottomEnd = 0.dp, bottomStart = 0.dp
		)
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.aspectRatio(0.9f)
				.clickable {
					onClick()
				}
		) {
			AnimatedVisibility(
				visible = isSelected,
				enter = slideInVertically(
					initialOffsetY = { it },
				),
				exit = slideOutVertically(
					targetOffsetY = { it }
				)
			) {
				Card(
					modifier = Modifier
						.fillMaxSize()
						.padding(top = 4.dp),
					shape = RoundedCornerShape(
						topStart = 20.dp, topEnd = 20.dp,
						bottomStart = 0.dp, bottomEnd = 0.dp,
					)
				) {  }
			}
			Image(
				vectorResource(navTarget.icon),
				contentDescription = null,
				modifier = Modifier
					.scale(navTarget.iconScale)
					.aspectRatio(1f)
					.padding(8.dp)
			)
			Text(
				text = navTarget.title,
				Modifier
					.padding(top = 68.dp)
				,
				fontSize = 10.sp
			)
		}
	}
}