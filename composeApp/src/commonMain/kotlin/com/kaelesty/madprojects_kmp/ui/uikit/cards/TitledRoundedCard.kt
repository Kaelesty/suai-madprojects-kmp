package com.kaelesty.madprojects_kmp.ui.uikit.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitledRoundedCard(
	title: String,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	Column(
		modifier = modifier
			.background(Color.Transparent)
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.body2.copy(
				fontSize = 24.sp
			),
			modifier = Modifier
				.offset(x = 24.dp)
		)
		StyledRoundedCard(
			modifier = Modifier
				.fillMaxWidth()
				.offset(y= (- 8).dp)
		) {
			content()
		}
	}
}