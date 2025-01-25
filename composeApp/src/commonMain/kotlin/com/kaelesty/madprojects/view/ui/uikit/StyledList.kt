package com.kaelesty.madprojects_kmp.ui.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.close
import madprojects.composeapp.generated.resources.right_arrow
import org.jetbrains.compose.resources.vectorResource

@Composable
fun <T> StyledList(
    items: List<T>,
    itemTitle: (T) -> String,
    onItemClick: ((T) -> Unit)?,
    leadingItem: String?,
    onLeadingClick: (() -> Unit)?,
    onDeleteItem: ((T) -> Unit)?,
    trailingItem: String? = null,
    onTrailingClick: (() -> Unit)? = null
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        leadingItem?.let { leadingItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        brush = Brush.linearGradient(
                            Styled.uiKit().colors().gradient,
                            tileMode = TileMode.Decal
                        ),
                        height = 2f
                    )
                    .clickable {
                        onLeadingClick?.let { it() }
                    }
            ) {
                Text(
                    text = leadingItem,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp,
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
                Icon(
                    vectorResource(Res.drawable.right_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
        }
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        brush = Brush.linearGradient(
                            Styled.uiKit().colors().gradient,
                            tileMode = TileMode.Decal
                        ),
                        height = 2f
                    )
                    .clickable {
                        onItemClick?.let { it(item) }
                    }
                    .padding(vertical = 2.dp)
            ) {
                Text(
                    text = itemTitle(item),
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp,
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
                onItemClick?.let {
                    Icon(
                        vectorResource(Res.drawable.right_arrow),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                onDeleteItem?.let {
                    Icon(
                        vectorResource(Res.drawable.close),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                it(item)
                            }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        trailingItem?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = trailingItem,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp,
                    ),
                    modifier = Modifier
                        .clickable { onTrailingClick?.let { it() } }
                )
            }
        }
    }

}
