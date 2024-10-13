package com.kaelesty.madprojects_kmp.ui.uikit.cards

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
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
			.padding(8.dp)
			.shadow(
				shape = RoundedCornerShape(16.dp),
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			),
		shape = RoundedCornerShape(16.dp)
	) {
		content()
	}
}