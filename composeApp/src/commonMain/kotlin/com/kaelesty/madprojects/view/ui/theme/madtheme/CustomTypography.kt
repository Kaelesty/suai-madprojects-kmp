package com.kaelesty.madprojects_kmp.ui.theme.madtheme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class CustomTypography(
	val logo: TextStyle,
	val welcome: TextStyle,
	val button: TextStyle,
	val inputFieldHeader: TextStyle,
	val inputFieldValue: TextStyle,
	val topBarHeader: TextStyle,
	val blockHeader: TextStyle,
	val contentMain: TextStyle,
	val contentSecondary: TextStyle,
)