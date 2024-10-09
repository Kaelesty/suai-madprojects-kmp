package com.kaelesty.madprojects_kmp.ui.lock

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard

@Composable
fun Loading() {
	Dialog(
		onDismissRequest = {}
	) {
		StyledCard(
			modifier = Modifier
				.size(80.dp)
				.padding(16.dp)
		) {
			CircularProgressIndicator(
				modifier = Modifier.fillMaxSize()
			)
		}
	}
}