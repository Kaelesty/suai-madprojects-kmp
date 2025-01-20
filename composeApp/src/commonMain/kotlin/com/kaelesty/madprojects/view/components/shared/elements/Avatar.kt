package com.kaelesty.madprojects.view.components.shared.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.amogus
import org.jetbrains.compose.resources.painterResource

@Composable
fun Avatar(
    url: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        null,
        modifier = modifier
            .padding(4.dp)
            .clip(CircleShape)
            .size(45.dp),
        contentScale = ContentScale.FillBounds,
        error = painterResource(Res.drawable.amogus)
    )
}