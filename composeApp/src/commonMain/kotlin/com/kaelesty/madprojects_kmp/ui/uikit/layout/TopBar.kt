package com.kaelesty.madprojects_kmp.ui.uikit.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.blocs.project.bottomBorder

@Composable
fun TopBar(title: String) {
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
                text = title,
                style = MaterialTheme.typography.body2
                    .copy(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraLight
                    ),
                modifier = Modifier
                    .offset(y = (-4).dp)
            )
        }
    }
}