package com.kaelesty.madprojects.view.ui.uikit.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kaelesty.madprojects.view.ui.experimental.Styled

@Composable
fun CardErrorText(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Styled.uiKit().ErrorText(text)
    }
}

@Composable
fun CardText(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Styled.uiKit().ErrorText(text)
    }
}