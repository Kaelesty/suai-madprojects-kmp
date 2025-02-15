package com.kaelesty.madprojects.view.components.main.project.kanban

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.madprojects.domain.repos.socket.KanbanState
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.extensions.toReadableTime
import com.kaelesty.madprojects_kmp.ui.theme.hexToColor
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.StyledDropdown
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.chevron_down
import madprojects.composeapp.generated.resources.chevron_up
import madprojects.composeapp.generated.resources.close
import madprojects.composeapp.generated.resources.move_dots
import madprojects.composeapp.generated.resources.right_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun KanbanContent(
    component: KanbanComponent
) {
    val state by component.state.collectAsState()
    val selectedColumn = state.selectedColumnId?.let {
        state.kanban.columns.firstOrNull { column -> column.id == it}
    }
    var showChangeColumnDialog by remember { mutableStateOf<KanbanState.Kard?>(null) }
    showChangeColumnDialog?.let { kard ->
        ChangeColumnDialog(
            kanban = state.kanban,
            kard = kard,
            selectedColumnId = state.kanban.columns.firstOrNull { it.kards.contains(kard) }?.id ?: -1,
            onDismiss = { showChangeColumnDialog = null },
            onSubmit = { newColumn, atStart ->
                component.setColumn(kard.id, newColumn, atStart)
            }
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    if (showAddDialog) {
        AddDialog(
            onCreateKard = { name, desc, columnId -> component.createKard(name, columnId) },
            onCreateColumn = { name, color ->
                component.createColumn(name, color)
            },
            onDismiss = {
                showAddDialog = false
            },
            currentColumnId = selectedColumn?.id ?: -1
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = { showAddDialog = true }
        ) {
            Icon(
                painter = painterResource(Res.drawable.right_arrow),
                null
            )
        }
    }

    Column {
        StyledDropdown(
            selectedItem = selectedColumn?.let {
                it.id to it.name
            },
            values = state.kanban.columns.map { it.id to it.name },
            modifier = Modifier,
            onItemSelection = {
                component.selectColumn(it.first)
            },
            closeOnSelect = true,
            fontSize = 20.sp,
            itemTitle = {
                it.second
            },
        )
        Spacer(Modifier.height(8.dp))
        selectedColumn?.let { selectedColumn ->
            LazyColumn {
                items(selectedColumn.kards, key = { it.id }) {
                    Kard(
                        kard = it,
                        onEdit = { kard, title, desc ->
                            component.editKard(kard, title, desc)
                        },
                        onDelete = {
                            component.deleteKard(it.id)
                        },
                        onUp = {
                            component.upKard(it.id)
                        },
                        onDown = {
                            component.downKard(it.id)
                        },
                        onChangeColumn = {
                            showChangeColumnDialog = it
                        },
                        modifier = Modifier,
                        colorHex = selectedColumn.color
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun Kard(
    kard: KanbanState.Kard,
    onEdit: (KanbanState.Kard, String, String) -> Unit,
    onDelete: (KanbanState.Kard) -> Unit,
    onUp: (() -> Unit)?,
    onDown: (() -> Unit)?,
    onChangeColumn: (() -> Unit)?,
    modifier: Modifier = Modifier,
    colorHex: String,
) {

    var showMoreDialog by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (showMoreDialog) {
        ShowMoreDialog(
            kard = kard,
            onDismiss = {
                showMoreDialog = false
            }
        )
    } else if (showEditDialog) {
        EditDialog(
            title = "Редактировать ${kard.title}",
            initialName = kard.title,
            initialDesc = kard.desc,
            onDismiss = {
                showEditDialog = false
            },
            onSubmit = { name, desc ->
                onEdit(kard, name, desc)
            }
        )
    } else if (showDeleteDialog) {
        DeleteDialog(
            itemName = "карточку ${kard.title}",
            onDismiss = {
                showDeleteDialog = false
            },
            onSubmit = {
                onDelete(kard)
            }
        )
    }

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    StyledRoundedCard(
        modifier = modifier,
        topBorderColor = hexToColor(colorHex)
    ) {
        Row(
            modifier = Modifier
                .height(160.dp)
                .padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 12.dp)
                    .weight(0.85f),
            ) {
                var lineCount by remember {
                    mutableStateOf(1)
                }
                Text(
                    text = kard.title,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 28.sp
                    ),
                    onTextLayout = {
                        lineCount = it.lineCount
                    }
                )
                Spacer(Modifier.weight(1f))
                if (lineCount < 4) {
                    Text(
                        text = "Автор: ${kard.authorName}",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                }
                if (lineCount < 3) {
                    Text(
                        text = "Создано: ${kard.createdTimeMillis.toReadableTime()}",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Изменено: ${kard.updateTimeMillis.toReadableTime()}",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(0.2f)
            ) {
                IconButton(
                    onClick = { menuExpanded = true }
                ) {
                    Icon(
                        vectorResource(Res.drawable.move_dots),
                        modifier = Modifier.size(30.dp),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = (-160).dp)
                ) {
                    onChangeColumn?.let {
                        DropdownMenuItem(
                            onClick = { it() },
                        ) {
                            Text("Переместить")
                        }
                    }
                    DropdownMenuItem(
                        onClick = { showEditDialog = true },
                    ) {
                        Text("Редактировать")
                    }
                    DropdownMenuItem(
                        onClick = { showDeleteDialog = true },
                    ) {
                        Text("Удалить")
                    }
                }

                if (onUp != null && onDown != null) {
                    IconButton(
                        onClick = { onUp() }
                    ) {
                        Icon(
                            vectorResource(Res.drawable.chevron_up),
                            modifier = Modifier.size(30.dp),
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = { onDown() }
                    ) {
                        Icon(
                            vectorResource(Res.drawable.chevron_down),
                            modifier = Modifier.size(30.dp),
                            contentDescription = null
                        )
                    }
                } else if (onUp != null) {
                    IconButton(
                        onClick = { onUp() }
                    ) {
                        Icon(
                            vectorResource(Res.drawable.chevron_up),
                            modifier = Modifier.size(30.dp),
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.weight(1f))
                } else if (onDown != null) {
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { onDown() }
                    ) {
                        Icon(
                            vectorResource(Res.drawable.chevron_down),
                            modifier = Modifier.size(30.dp),
                            contentDescription = null
                        )
                    }
                }





                IconButton(
                    onClick = { showMoreDialog = true }
                ) {
                    Icon(
                        vectorResource(Res.drawable.right_arrow),
                        modifier = Modifier.size(40.dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun EditDialog(
    title: String,
    initialName: String,
    initialDesc: String,
    showSecond: Boolean = true,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit,
    nameLimit: Int = 50,
    descLimit: Int = 1024,
) {

    var name by rememberSaveable {
        mutableStateOf(initialName)
    }
    var desc by rememberSaveable {
        mutableStateOf(initialDesc)
    }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 28.sp
                    ),
                )
                Spacer(Modifier.height(4.dp))
                TitledTextField(
                    title = "Название",
                    text = name,
                    onValueChange = {
                        if (name.length > nameLimit) {
                            errorMessage = "Слишком длинное имя (${name.length}/$nameLimit)"
                        }
                        name = it
                    }
                )
                Spacer(Modifier.height(8.dp))
                if (showSecond) {
                    TitledTextField(
                        title = "Описание",
                        text = desc,
                        onValueChange = {
                            if (desc.length > descLimit) {
                                errorMessage =
                                    "Слишком длинное описание (${desc.length}/$descLimit)"
                            }
                            desc = it
                        },
                        isSingleLine = false
                    )
                }
                Spacer(Modifier.height(4.dp))
                errorMessage?.let {
                    Styled.uiKit().ErrorText(
                        text = it
                    )
                }
                Spacer(Modifier.height(12.dp))
                StyledButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = "Подтвердить",
                    onClick = {
                        if (errorMessage == null) {
                            errorMessage = null
                            onSubmit(name, desc)
                            onDismiss()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChangeColumnDialog(
    kanban: KanbanState,
    kard: KanbanState.Kard,
    selectedColumnId: Int,
    onDismiss: () -> Unit,
    onSubmit: (Int, Boolean) -> Unit, // true -> start, false -> end
) {

    val vals = listOf(
        "Поместить в конец",
        "Поместить в начало",
    )
    var selectedItem by remember { mutableStateOf(vals[0]) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Переместить ${kard.title}",
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 28.sp
                    ),
                )
                Spacer(Modifier.height(8.dp))
                StyledDropdown(
                    selectedItem = selectedItem,
                    values = vals,
                    onItemSelection = {
                        selectedItem = it
                    },
                    itemTitle = { it }
                )
                Spacer(Modifier.height(8.dp))
                kanban.columns
                    .filter { it.id != selectedColumnId }
                    .forEach {
                        Row(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .bottomBorder(
                                    brush = Brush.linearGradient(
                                        Styled.uiKit().colors().gradient,
                                        tileMode = TileMode.Decal
                                    ),
                                    height = 2f
                                )
                                .clickable {
                                    onSubmit(it.id, selectedItem == vals[0])
                                    onDismiss()
                                }
                        ) {
                            Text(
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                ),
                                text = it.name,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            Icon(
                                vectorResource(Res.drawable.right_arrow),
                                null,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DeleteDialog(
    itemName: String,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Удалить $itemName?",
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 28.sp
                    ),
                )
                Spacer(Modifier.height(4.dp))
                Styled.uiKit().ErrorText(
                    text = "Это действие не может быть отменено"
                )
                Spacer(Modifier.height(8.dp))
                StyledButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = "Удалить",
                    onClick = {
                        onSubmit()
                        onDismiss()
                    }
                )
            }
        }
    }
}


@Composable
fun ShowMoreDialog(
    kard: KanbanState.Kard,
    onDismiss: () -> Unit,
) {

    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .heightIn(min = 280.dp, max = 480.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = kard.title,
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 28.sp
                        ),
                        modifier = Modifier

                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = kard.desc,
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light
                        ),
                        modifier = Modifier

                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Автор: ${kard.authorName}",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Создано: ${kard.createdTimeMillis.toReadableTime()}",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Изменено: ${kard.updateTimeMillis.toReadableTime()}",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        ),
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EditColumnsDialog(
    kanban: KanbanState,
    onDelete: (Int) -> Unit,
    onUp: (Int) -> Unit,
    onDown: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Редактирование колонок",
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 28.sp
                    ),
                )
                Spacer(Modifier.height(8.dp))
                kanban.columns
                    .forEachIndexed { index, it ->
                        Row(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .bottomBorder(
                                    brush = Brush.linearGradient(
                                        Styled.uiKit().colors().gradient,
                                        tileMode = TileMode.Decal
                                    ),
                                    height = 2f
                                )
                        ) {
                            Text(
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                ),
                                text = it.name,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            if (index != 0) {
                                IconButton(
                                    onClick = { onUp(it.id) }
                                ) {
                                    Icon(
                                        vectorResource(Res.drawable.chevron_up),
                                        null,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                            }
                            if (index != kanban.columns.size - 1) {
                                IconButton(
                                    onClick = { onDown(it.id) }
                                ) {
                                    Icon(
                                        vectorResource(Res.drawable.chevron_down),
                                        null,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { onDelete(it.id) }
                            ) {
                                Icon(
                                    vectorResource(Res.drawable.close),
                                    null,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }
                        }
                    }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}