package com.kaelesty.madprojects_kmp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.just_another_hand_regular
import madprojects.composeapp.generated.resources.roboto_flex
import org.jetbrains.compose.resources.Font

@Composable
fun justAnotherHandFamily() = FontFamily(
	Font(Res.font.just_another_hand_regular)
)

@Composable
fun robotoFlex() = FontFamily(
	Font(Res.font.roboto_flex)
)