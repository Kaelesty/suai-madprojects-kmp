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
    override val github_0: Color
        get() = hexToColor("CCE8FF")
    override val github_25: Color
        get() = hexToColor("CCE8FF")
    override val github_50: Color
        get() = hexToColor("66A3E6")
    override val github_75: Color
        get() = hexToColor("337BCC")
    override val github_100: Color
        get() = hexToColor("005AAA")
}

object DarkColors: Styled.Colors {
    override val errorRed = Color.Red
    override val gradient: List<Color>
        get() = listOf(
            hexToColor("005AAA"),
            hexToColor("AB3A8D"),
            hexToColor("E4003A")
        )
    override val github_0: Color
        get() = hexToColor("CCE8FF")
    override val github_25: Color
        get() = hexToColor("CCE8FF")
    override val github_50: Color
        get() = hexToColor("66A3E6")
    override val github_75: Color
        get() = hexToColor("337BCC")
    override val github_100: Color
        get() = hexToColor("005AAA")
}