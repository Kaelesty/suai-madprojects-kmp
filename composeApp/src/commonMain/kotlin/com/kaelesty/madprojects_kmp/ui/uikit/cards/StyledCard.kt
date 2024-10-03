package com.kaelesty.madprojects_kmp.ui.uikit.cards

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.blocs.welcome.topBorder

@Composable
fun StyledCard(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	val gradientColors = listOf(
		MaterialTheme.colors.secondary,
		MaterialTheme.colors.secondaryVariant,
		MaterialTheme.colors.onSecondary
	)
	Card(
		modifier = modifier
			.shadow(
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			)
			.topBorder(
				brush = Brush.linearGradient(gradientColors, tileMode = TileMode.Decal),
				height = 8f
			)
		,
		shape = MaterialTheme.shapes.medium.copy(
			CornerSize(0), CornerSize(0), CornerSize(0), CornerSize(0),
		),
	) {
		content()
	}
}