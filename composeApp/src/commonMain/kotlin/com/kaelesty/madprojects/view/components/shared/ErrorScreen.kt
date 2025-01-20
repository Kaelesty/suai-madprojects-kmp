package com.kaelesty.madprojects.view.components.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import com.kaelesty.madprojects.view.ui.experimental.Styled
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.error
import org.jetbrains.compose.resources.painterResource

@Composable
fun ErrorScreen() {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Res.drawable.error),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
            )
            Styled.uiKit().ErrorText(
                "Произошла неизвесная ошибка и приложение упало. Мы уже работаем над этим."
            )
        }
    }
}