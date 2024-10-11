package com.kaelesty.madprojects_kmp.blocs.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.ui.uikit.text.TypewriterText

@Composable
fun WelcomeContent(
	component: WelcomeComponent
) {
	val shouldPlayAnimation = component.shouldPlayAnimation
	var cardVisible by remember { mutableStateOf(!shouldPlayAnimation) }

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		AnimatedVisibility(
			visible = cardVisible,
			enter = expandVertically(animationSpec = tween(durationMillis = 400))
		) {
			AuthCard(
				onEnter = { component.enter() },
				onRegister = { component.register() },
				modifier = Modifier
					.fillMaxWidth(0.85f)
					.fillMaxHeight(0.5f)
				,
			)
		}
		TypewriterText(
			text = "madprojects",
			style = MaterialTheme.typography.caption,
			onFinish = {
				cardVisible = true
			},
			modifier = Modifier.offset(y = (-232).dp),
			playAnimation = shouldPlayAnimation
		)
	}
}