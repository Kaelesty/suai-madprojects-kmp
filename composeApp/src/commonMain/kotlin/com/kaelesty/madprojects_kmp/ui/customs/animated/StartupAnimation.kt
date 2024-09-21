package com.kaelesty.madprojects_kmp.ui.customs.animated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.ui.customs.cards.AuthCard
import com.kaelesty.madprojects_kmp.ui.customs.text.TypewriterText

@Composable
fun StartupAnimation() {

	var cardVisible by remember { mutableStateOf(false) }

	TypewriterText(
		text = "madprojects",
		style = MaterialTheme.typography.caption,
		onFinish = {
			cardVisible = true
		}
	)

	AnimatedVisibility(
		visible = cardVisible,
		enter = expandIn()
	) {

		AuthCard(
			modifier = Modifier
				.fillMaxWidth(0.85f)
				.fillMaxHeight(0.5f)
		)
	}
}