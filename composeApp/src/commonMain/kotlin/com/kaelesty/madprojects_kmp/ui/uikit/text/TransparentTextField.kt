package com.kaelesty.madprojects_kmp.ui.uikit.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun TransparentTextField(
    text: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 16,
    placeholder: String = ""
) {
    TextField(
        value = text,
        modifier = modifier,
        onValueChange = onValueChange,
        singleLine = singleLine,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        textStyle = MaterialTheme.typography.body2.copy(
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Thin
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Thin
                ),
            )
        }
    )
}