package com.kaelesty.madprojects_kmp.ui.uikit.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StyledTextField(
	text: String,
	modifier: Modifier = Modifier,
	onValueChange: (String) -> Unit,
	singleLine: Boolean = true,
	isPassword: Boolean = false,
	imeAction: ImeAction? = null,
	keyboardType: KeyboardType? = null,
	maxLines: Int = 1,
	height: Dp = 55.dp,
	textSize: TextUnit = 20.sp,
) {
	Card(
		modifier = modifier
			.shadow(
				elevation = 6.dp,
				spotColor = DefaultShadowColor,
				ambientColor = DefaultShadowColor
			)
			.height(height)
		,
		shape = MaterialTheme.shapes.medium.copy(
			CornerSize(0), CornerSize(0), CornerSize(0), CornerSize(0),
		),
	) {
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			TextField(
				value = text,
				modifier = modifier
					.fillMaxSize(),
				onValueChange = onValueChange,
				singleLine = singleLine,
				colors = TextFieldDefaults.textFieldColors(
					backgroundColor = Color.Transparent,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Color.Transparent,
				),
				textStyle = MaterialTheme.typography.body2.copy(
					fontSize = textSize,
					textAlign = TextAlign.Start,
				),
				visualTransformation = if (isPassword) PasswordVisualTransformation()
				else VisualTransformation.None,
				keyboardOptions = KeyboardOptions(
					imeAction = imeAction ?: ImeAction.Done,
					keyboardType = keyboardType ?: KeyboardType.Text
				),
				maxLines = maxLines,
			)
			Spacer(Modifier.weight(1f))
		}
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
	imeAction: ImeAction? = null,
	keyboardType: KeyboardType? = null,
	maxLines: Int = 1,
	titleSize: TextUnit = 22.sp,
	textSize: TextUnit = 20.sp,
	height: Dp = 55.dp,
	outerModifier: Modifier = Modifier
) {

	Column(
		modifier = outerModifier
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.body2.copy(
				fontSize = titleSize,
			),
		)
		Spacer(modifier = Modifier.height(6.dp))
		StyledTextField(
			text,
			modifier,
			onValueChange,
			isPassword = isPassword,
			singleLine = isSingleLine,
			imeAction = imeAction,
			maxLines = maxLines,
			keyboardType = keyboardType,
			height = height,
			textSize = textSize,
		)
	}
}

@Composable
fun TinyTitledTextField(
	text: String,
	title: String,
	isPassword: Boolean = false,
	modifier: Modifier = Modifier,
	outerModifier: Modifier = Modifier,
	onValueChange: (String) -> Unit,
	isSingleLine: Boolean = true,
	imeAction: ImeAction? = null,
	keyboardType: KeyboardType? = null,
	maxLines: Int = 1,
	height: Dp = 50.dp
) {
	TitledTextField(
		text = text,
		title = title,
		onValueChange = {
			onValueChange(it)
		},
		keyboardType = keyboardType,
		titleSize = 16.sp,
		height = height,
		textSize = 18.sp,
		imeAction = imeAction,
		isSingleLine = isSingleLine,
		modifier = modifier,
		isPassword = isPassword,
		maxLines = maxLines,
		outerModifier = outerModifier
	)
}