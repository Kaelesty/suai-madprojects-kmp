package com.kaelesty.madprojects.view.ui.uikit.dialogues

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField

@Composable
fun StyledCreationDialog(
    isVisible: Boolean,
    title: String,
    actionTitle: String,
    onAction: (String) -> Unit,
    onHide: () -> Unit,
    error: String?,
) {

    var text by remember { mutableStateOf("") }
    if (!isVisible) return
    Dialog(
        onDismissRequest = onHide
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitledTextField(
                    text = text,
                    title = title,
                    onValueChange = { text = it }
                )
                Spacer(Modifier.height(4.dp))
                StyledButton(
                    text = actionTitle,
                    onClick = {
                        onAction(text)
                    },
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Spacer(Modifier.height(4.dp))
                error?.let {
                    Styled.uiKit().ErrorText(it)
                }
            }
        }

    }
}