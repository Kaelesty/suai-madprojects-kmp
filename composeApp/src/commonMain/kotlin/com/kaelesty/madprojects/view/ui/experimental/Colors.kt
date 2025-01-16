package com.kaelesty.madprojects_kmp.ui.experimental

import androidx.compose.ui.graphics.Color
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.theme.hexToColor

object LightColors: Styled.Colors {
    override val errorRed = Color.Red
    override val gradient: List<Color>
        get() = listOf(
            hexToColor("005AAA"),
            hexToColor("AB3A8D"),
            hexToColor("E4003A")
        )
}

object DarkColors: Styled.Colors {
    override val errorRed = Color.Red
    override val gradient: List<Color>
        get() = listOf(
            hexToColor("005AAA"),
            hexToColor("AB3A8D"),
            hexToColor("E4003A")
        )
}