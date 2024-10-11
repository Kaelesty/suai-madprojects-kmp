package com.kaelesty.madprojects_kmp.ui.uikit.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TypewriterText(
	text: String,
	style: TextStyle,
	onFinish: @Composable () -> Unit = {},
	delayOptions: List<Long> = listOf(160, 180, 220, 400, 300),
	modifier: Modifier = Modifier,
	playAnimation: Boolean = true
) {

	// reference:
	// https://medium.com/make-apps-simple/typewriter-animation-in-jetpack-compose-2b0c7ee323c2

	var textToDisplay by remember {
		mutableStateOf(if (playAnimation) "" else text)
	}

	LaunchedEffect(
		key1 = text,
	) {
		if (!playAnimation) return@LaunchedEffect
		text.forEachIndexed { charIndex, _ ->
			textToDisplay = text
				.substring(
					startIndex = 0,
					endIndex = charIndex + 1,
				)
			delay(
				delayOptions[Random(12).nextInt(delayOptions.size - 1)]
			)
		}
	}

	Text(
		text = textToDisplay,
		style = style,
		modifier = modifier
	)

	if (textToDisplay.length == text.length) {
		onFinish()
	}
}