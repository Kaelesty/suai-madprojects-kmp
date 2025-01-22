package com.kaelesty.madprojects_kmp.ui.uikit.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp

@Composable
fun StyledRoundedCard(
	modifier: Modifier = Modifier,
	shape: RoundedCornerShape = RoundedCornerShape(16.dp),
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
			.shadow(
				shape = shape,
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			),
		shape = shape
	) {
		content()
	}
}