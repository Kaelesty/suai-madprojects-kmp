package com.kaelesty.madprojects_kmp.ui.uikit.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StyledTextField(
	text: String,
	modifier: Modifier = Modifier,
	onValueChange: (String) -> Unit,
	singleLine: Boolean = true,
	isPassword: Boolean = false,
) {
	Card(
		modifier = modifier
			.shadow(
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			)
			.height(60.dp)
		,
		shape = MaterialTheme.shapes.medium.copy(
			CornerSize(0), CornerSize(0), CornerSize(0), CornerSize(0),
		),
	) {
		TextField(
			value = text,
			modifier = modifier,
			onValueChange = onValueChange,
			singleLine = singleLine,
			colors = TextFieldDefaults.textFieldColors(
				backgroundColor = Color.Transparent,
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				disabledIndicatorColor = Color.Transparent,
			),
			textStyle = MaterialTheme.typography.body2,
			visualTransformation = if (isPassword) PasswordVisualTransformation()
				                   else VisualTransformation.None
		)
	}

}

@Composable
fun TitledTextField(
	text: String,
	title: String,
	isPassword: Boolean = false,
	modifier: Modifier = Modifier,
	onValueChange: (String) -> Unit,
	isSingleLine: Boolean = true,
) {

	Column {
		Text(
			text = title,
			style = MaterialTheme.typography.body2
		)
		Spacer(modifier = Modifier.height(10.dp))
		StyledTextField(
			text, modifier, onValueChange, isPassword = isPassword, singleLine = isSingleLine
		)
	}
}