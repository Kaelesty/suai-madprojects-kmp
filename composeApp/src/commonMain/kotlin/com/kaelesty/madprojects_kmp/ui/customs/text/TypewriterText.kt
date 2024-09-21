package com.kaelesty.madprojects_kmp.ui.customs.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TypewriterText(
	text: String,
	style: TextStyle,
	onFinish: @Composable () -> Unit = {},
	delayOptions: List<Long> = listOf(160, 180, 220, 400, 300)
) {

	// reference:
	// https://medium.com/make-apps-simple/typewriter-animation-in-jetpack-compose-2b0c7ee323c2

	var textIndex by remember {
		mutableStateOf(0)
	}
	var textToDisplay by remember {
		mutableStateOf("")
	}

	LaunchedEffect(
		key1 = text,
	) {
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
	)

	if (textToDisplay.length == text.length) {
		onFinish()
	}
}