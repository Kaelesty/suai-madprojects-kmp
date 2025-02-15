package com.kaelesty.madprojects_kmp.ui.uikit.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects.view.extensions.topBorder

@Composable
fun StyledRoundedCard(
	modifier: Modifier = Modifier,
	shape: RoundedCornerShape = RoundedCornerShape(16.dp),
	topBorderColor: Color = MaterialTheme.colors.surface,
	backgroundColor: Color = MaterialTheme.colors.surface,
	content: @Composable () -> Unit,
) {
	Card(
		modifier = modifier
			.shadow(
				shape = shape,
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			)
			.topBorder(
                brush = Brush.linearGradient(listOf(topBorderColor, topBorderColor)),
                height = 16f
            )
		,
		shape = shape,
		backgroundColor = backgroundColor
	) {
		content()
	}
}