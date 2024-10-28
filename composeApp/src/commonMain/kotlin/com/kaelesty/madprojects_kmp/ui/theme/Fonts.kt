package com.kaelesty.madprojects_kmp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.just_another_hand_regular
import madprojects.composeapp.generated.resources.roboto_black
import madprojects.composeapp.generated.resources.roboto_bold
import madprojects.composeapp.generated.resources.roboto_light
import madprojects.composeapp.generated.resources.roboto_regular
import madprojects.composeapp.generated.resources.roboto_thin
import org.jetbrains.compose.resources.Font

@Composable
fun justAnotherHandFamily() = FontFamily(
    Font(Res.font.just_another_hand_regular)
)

@Composable
fun robotoFlex() = FontFamily(
    Font(Res.font.roboto_regular, weight=FontWeight.Normal),
    Font(Res.font.roboto_bold, weight=FontWeight.Bold),
    Font(Res.font.roboto_thin, weight=FontWeight.Thin),
    Font(Res.font.roboto_light, weight=FontWeight.Light),
    Font(Res.font.roboto_black, weight=FontWeight.Black),
)

