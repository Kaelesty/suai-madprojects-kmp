package com.kaelesty.madprojects.view.components.main.project.kanban

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField
import io.github.mohammedalaamorsi.colorpicker.ColorPickerDialog
import io.github.mohammedalaamorsi.colorpicker.ColorPickerType
import io.github.mohammedalaamorsi.colorpicker.ext.toHex

enum class AddType {
    Kard, Column
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddDialog(
    onCreateKard: (String, String, Int) -> Unit,
    onCreateColumn: (String, String) -> Unit,
    onDismiss: () -> Unit,
    currentColumnId: Int,
) {
    var addType by remember { mutableStateOf<AddType?>(null) }
    var name by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Cyan) }

    var showColorPicker by remember { mutableStateOf(false) }
    CustomColorPickerDialog(
        show = showColorPicker,
        type = ColorPickerType.Circle(showAlphaBar = false),
        onDismissRequest = {
            showColorPicker = false
        },
        onPickedColor = {
            color = it
        },
    )

    Dialog(
        onDismiss,
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    vertical = 12.dp,
                    horizontal = 8.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (addType) {
                    AddType.Kard -> {
                        Text(
                            text = "Добавить карточку",
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 28.sp
                            ),
                        )
                        Spacer(Modifier.height(8.dp))
                        TitledTextField(
                            title = "Название",
                            text = name,
                            onValueChange = {
                                if (name.length > 50) {
                                    errorMessage = "Слишком длинное имя (${name.length}/50)"
                                }
                                name = it
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        StyledButton(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            text = "Добавить",
                            onClick = {
                                onCreateKard(name, "", currentColumnId)
                                onDismiss()
                            }
                        )
                    }
                    AddType.Column -> {
                        Text(
                            text = "Добавить колонку",
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 28.sp
                            ),
                        )
                        Spacer(Modifier.height(8.dp))
                        TitledTextField(
                            title = "Название",
                            text = name,
                            onValueChange = {
                                if (name.length > 25) {
                                    errorMessage = "Слишком длинное имя (${name.length}/25)"
                                }
                                name = it
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .clickable {
                                    showColorPicker = true
                                }
                        ) {
                            Text(
                                text = "Цвет: ",
                                style = MaterialTheme.typography.body2.copy(),
                            )
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(color = color, shape = RoundedCornerShape(16.dp))
                                    .border(width = 1.dp, color = Color.Black)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        StyledButton(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            text = "Добавить",
                            onClick = {
                                onCreateColumn(name, color.toHex(hexPrefix = false, includeAlpha = false))
                                onDismiss()
                            }
                        )
                    }
                    null -> {
                        StyledButton(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            text = "Добавить карточку",
                            onClick = { addType = AddType.Kard }
                        )
                        Spacer(Modifier.height(8.dp))
                        StyledButton(
                            modifier = Modifier.fillMaxWidth(0.7f),
                            text = "Добавить колонку",
                            onClick = { addType = AddType.Column }
                        )
                    }
                }
            }
        }
    }
}