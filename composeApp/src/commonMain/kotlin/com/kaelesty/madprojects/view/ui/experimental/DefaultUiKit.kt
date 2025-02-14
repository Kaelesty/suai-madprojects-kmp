package com.kaelesty.madprojects_kmp.ui.experimental

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.components.main.profile.CuratorMetaCard
import com.kaelesty.madprojects.view.components.main.profile.ProjectGroupsList
import com.kaelesty.madprojects.view.components.shared.elements.TopBar
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.theme.robotoFlex

@Immutable
abstract class DefaultUiKit: Styled.UiKit {

    @Composable
    override fun Text(text: String) {
        Text(
            text = text,
            style = typography().errorText().copy(
                color = Color.Black
            )
        )
    }

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

    @Composable
    override fun DefaultScreenScaffold(
        topBarTitle: String,
        isScrollable: Boolean,
        bottomBar: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ) {

        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopBar(topBarTitle)
            },
            bottomBar = {
                bottomBar()
            },
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(4.dp)
                    .let {
                        if (isScrollable) {
                            it.verticalScroll(scrollState)
                        }
                        else it
                    }
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}