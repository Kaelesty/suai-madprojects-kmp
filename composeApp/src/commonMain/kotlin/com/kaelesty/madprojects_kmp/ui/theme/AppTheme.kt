package com.kaelesty.madprojects_kmp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AppTheme(
	isDarkTheme: Boolean,
	content: @Composable () -> Unit,
) = MaterialTheme(
	content = content,
	colors = getCustomColors(isDarkTheme),
	typography = getCustomTypography(isDarkTheme),
	shapes = MaterialTheme.shapes,
)

@Composable
fun getCustomTypography(isDarkTheme: Boolean) = MaterialTheme.typography.copy(
	caption = TextStyle(
		fontFamily = justAnotherHandFamily(),
		fontSize = 60.sp
	),
	button = TextStyle(
		fontFamily = robotoFlex(),
		fontSize = 24.sp
	),
	body2 = TextStyle(
		fontFamily = robotoFlex(),
		fontSize = 20.sp,
		fontWeight = FontWeight.Light,
		color = Color.Black,
	),
	overline = TextStyle(
		fontFamily = robotoFlex(),
		fontSize = 18.sp,
		fontWeight = FontWeight.Light,
		color = Color.Red
	)
)

@Composable
fun getCustomColors(isDarkTheme: Boolean) = MaterialTheme.colors.copy(
	secondary = hexToColor("005AAA"),
	secondaryVariant = hexToColor("AB3A8D"),
	onSecondary = hexToColor("E4003A"),
	primary = hexToColor("005AAA")
)

fun hexToColor(hex: String): Color {
	val formattedHex = if (hex.startsWith("#")) hex else "#$hex"

	val colorInt = when (formattedHex.length) {
		7 -> {
			val r = formattedHex.substring(1, 3).toInt(16)
			val g = formattedHex.substring(3, 5).toInt(16)
			val b = formattedHex.substring(5, 7).toInt(16)
			Color(r, g, b, 255)
		}
		9 -> {
			val a = formattedHex.substring(1, 3).toInt(16)
			val r = formattedHex.substring(3, 5).toInt(16)
			val g = formattedHex.substring(5, 7).toInt(16)
			val b = formattedHex.substring(7, 9).toInt(16)
			Color(r, g, b, a)
		}
		else -> throw IllegalArgumentException("Invalid hex color format")
	}

	return colorInt
}