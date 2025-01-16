package com.kaelesty.madprojects.view.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

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