package com.kaelesty.madprojects_kmp.ui.uikit.buttons

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StyledButton(
	modifier: Modifier,
	text: String,
	onClick: () -> Unit,
) {

	Button(
		onClick,
		modifier = modifier,
		colors = ButtonDefaults.buttonColors(
			backgroundColor = MaterialTheme.colors.primary
		)
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.button
		)
	}
}