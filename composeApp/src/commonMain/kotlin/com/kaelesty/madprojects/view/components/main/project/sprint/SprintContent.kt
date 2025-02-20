package com.kaelesty.madprojects.view.components.main.project.sprint

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.repos.socket.KanbanState
import com.kaelesty.madprojects.view.components.main.project.kanban.Kard
import com.kaelesty.madprojects.view.components.shared.LoadingScreen
import com.kaelesty.madprojects.view.ui.uikit.cards.CardErrorText
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.StyledDropdown

@Composable
fun SprintContent(
    component: SprintComponent
) {
    val state by component.state.collectAsState()

    when (val instance = state) {
        SprintComponent.State.Error -> StyledRoundedCard(
            modifier = Modifier
                .height(128.dp)
                .fillMaxWidth(0.9f)
        ) {
            CardErrorText("Ошибка загрузки данных")
        }
        SprintComponent.State.Loading -> {
            LoadingScreen()
        }
        is SprintComponent.State.Main -> {
            SprintContentMain(component, instance)
        }
    }
}

@Composable
fun SprintContentMain(
    component: SprintComponent,
    state: SprintComponent.State.Main
) {

    var selectedColumnId by remember {
        mutableStateOf(-1)
    }

    LaunchedEffect(state) {
        selectedColumnId = state.kanbanState.columns.firstOrNull()?.id ?: -1
    }

    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitledRoundedCard(
            title = "Спринт / ${state.sprintMeta.title}"
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Text(
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                    text = "Дата начала: ${state.sprintMeta.startDate}"
                )
                Text(
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                    text = "Планируемая дата завершения: ${state.sprintMeta.supposedEndDate}"
                )
                state.sprintMeta.actualEndDate?.let {
                    Text(
                        fontStyle = MaterialTheme.typography.body2.fontStyle,
                        text = "Дата завершения: ${it}"
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        TitledRoundedCard(
            title = "Описание"
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Text(
                    fontStyle = MaterialTheme.typography.body2.fontStyle,
                    text = state.sprintMeta.desc
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Задачи",
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 24.sp
                ),
                modifier = Modifier
                    .offset(x = 16.dp)
            )
        }
        StyledDropdown(
            selectedItem = state.kanbanState.columns.firstOrNull { it.id == selectedColumnId },
            values = state.kanbanState.columns,
            onItemSelection = { column ->
                selectedColumnId = state.kanbanState.columns
                    .firstOrNull { it.id == column.id }?.id ?: -1
            },
            closeOnSelect = true,
            itemTitle = { it.name }
        )
        Spacer(Modifier.height(8.dp))
        val column = state.kanbanState.columns.firstOrNull { it.id == selectedColumnId }
        column?.let { column ->
            LazyColumn {
                items(column.kards, key = { it.id }) {
                    Kard(
                        kard = it,
                        onEdit = { _, _, _ -> },
                        onDelete = {},
                        onUp = {},
                        onDown = {},
                        onChangeColumn = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { component.toKanban() }
                        ,
                        colorHex = column.color,
                        showControls = false
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}