package com.kaelesty.madprojects_kmp.ui.customs.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.ui.customs.buttons.CommonButton

@Composable
fun AuthCard(
	modifier: Modifier = Modifier,
	onEnter: () -> Unit,
	onRegister: () -> Unit,
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
				brush = Brush.linearGradient(gradientColors),
				height = 8f
			)
		,
		shape = MaterialTheme.shapes.medium.copy(
			CornerSize(0), CornerSize(0), CornerSize(0),CornerSize(0),
		),
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.padding(top = 60.dp)
		) {
			Text("Привет!",
				 fontSize = 30.sp)
			Text("С чего начнем?", fontSize = 30.sp)
			Spacer(Modifier.height(8.dp))
			CommonButton(
				Modifier.fillMaxWidth(0.8f),
				text = "Вход",
				onClick = { onEnter() }
			)
			Spacer(Modifier.height(8.dp))
			CommonButton(
				Modifier.fillMaxWidth(0.8f),
				text = "Регистрация",
				onClick = { onRegister() }
			)
		}
	}
}

fun Modifier.topBorder(
	brush: Brush,
	height: Float,
) = this.drawWithContent {
	drawContent()
	drawLine(
		brush = brush,
		start = Offset(0f, 0f),
		end = Offset(size.width, 0f),
		strokeWidth = height,
	)
}