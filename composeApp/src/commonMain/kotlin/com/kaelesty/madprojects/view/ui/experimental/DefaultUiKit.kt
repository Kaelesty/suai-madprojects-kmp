package com.kaelesty.madprojects_kmp.ui.experimental

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.theme.robotoFlex

@Immutable
abstract class DefaultUiKit: Styled.UiKit {

    @Composable
    override fun typography() = object:  Styled.Typography {
        @Composable
        override fun errorText() = TextStyle(
                fontFamily = robotoFlex(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = Color.Red
            )
    }

    @Composable
    override fun ErrorText(text: String) {
        Text(
            text = text,
            style = typography().errorText()
        )
    }
}