package com.kaelesty.madprojects_kmp.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun AsyncImage(model: String, modifier: Modifier) {
	coil3.compose.AsyncImage(
		model = model,
		null,
		modifier = Modifier
			.size(80.dp)
			.aspectRatio(1f)
	)
}