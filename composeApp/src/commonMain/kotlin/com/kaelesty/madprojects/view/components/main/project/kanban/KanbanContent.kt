package com.kaelesty.madprojects.view.components.main.project.kanban

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.ui.theme.hexToColor
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.StyledDropdown

@Composable
fun KanbanContent(
    component: KanbanComponent
) {
    val state by component.state.collectAsState()
    val selectedColumn = state.selectedColumnId?.let {
        state.kanban.columns.firstOrNull { column -> column.id == it}
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

        selectedColumn?.let { selectedColumn ->
            LazyColumn {
                items(selectedColumn.kards, key = { it.id}) {
                    Text(it.title)
                }
            }
        }
    }
}