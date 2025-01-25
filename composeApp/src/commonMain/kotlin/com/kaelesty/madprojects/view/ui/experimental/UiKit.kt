package com.kaelesty.madprojects.view.ui.experimental

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.kaelesty.madprojects_kmp.ui.experimental.DarkColors
import com.kaelesty.madprojects_kmp.ui.experimental.DefaultUiKit
import com.kaelesty.madprojects_kmp.ui.experimental.LightColors

@Immutable
object Styled {

    interface Colors {
        val errorRed: Color
        val gradient: List<Color>
    }

    interface Typography {
        @Composable
        fun errorText(): TextStyle
    }

    interface UiKit {

        @Composable
        fun ErrorText(text: String)

        @Composable
        fun Text(text: String)

        fun colors(): Colors

        @Composable
        fun typography(): Typography

        @Composable
        fun DefaultScreenScaffold(
            topBarTitle: String,
            isScrollable: Boolean,
            bottomBar: @Composable () -> Unit,
            content: @Composable () -> Unit,
        )
    }

    private val lightUiKit = @Immutable object : DefaultUiKit() {
        override fun colors(): Colors = LightColors
    }

    private val darkUiKit = @Immutable object : DefaultUiKit() {
        override fun colors(): Colors = DarkColors
    }

    fun uiKit(isDarkTheme: Boolean = true): UiKit = if (isDarkTheme) darkUiKit else lightUiKit
}