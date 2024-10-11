package com.kaelesty.madprojects_kmp.ui.lock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard

@Composable
fun UnexpectedExceptionDialog(
	e: Exception,
	onDismiss: () -> Unit,
) {
	Dialog(
		onDismissRequest = {}
	) {
		StyledCard {
			Column(
				modifier = Modifier
					.padding(8.dp)
			) {
				Text("Непредвиденная ошибка!")
				Text(e.message ?: "<no message>")
				StyledButton(
					modifier = Modifier.fillMaxWidth(),
					text = "OK"
				) {
					onDismiss()
				}
			}
		}
	}
}