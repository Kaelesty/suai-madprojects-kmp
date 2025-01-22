package com.kaelesty.madprojects.view.components.main.project_creation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.TitledDropdown
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField

@Composable
fun ProjectCreationContent(
    component: ProjectCreationComponent
) {

    val state by component.state.collectAsState()

    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = "Создание проекта"
    ) {
        StyledRoundedCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                TitledTextField(
                    text = state.title,
                    title = "Название",
                    onValueChange = {
                        component.updateTitle(it)
                    },
                    imeAction = ImeAction.Next,
                )
                Spacer(Modifier.height(4.dp))
                TitledTextField(
                    text = state.desc,
                    title = "Описание",
                    onValueChange = {
                        component.updateDesc(it)
                    },
                    maxLines = 10,
                    height = 128.dp,
                    imeAction = ImeAction.Next,
                    isSingleLine = false
                )
                Spacer(Modifier.height(4.dp))
                TitledTextField(
                    text = state.maxMembersCount.toString(),
                    title = "Число учасников",
                    onValueChange = {
                        component.updateMaxMembersCount(it.toIntOrNull() ?: 0)
                    },
                    imeAction = ImeAction.Done,
                )
                Spacer(Modifier.height(4.dp))
                StyledList(
                    items = state.repoLinks,
                    itemTitle = { it },
                    leadingItem = "Добавить",
                    onLeadingClick = {  },
                    onDeleteItem = { component.removeRepoLink(it) },
                    onItemClick = {}
                )
                Spacer(Modifier.height(4.dp))
                TitledDropdown(
                    title = "Преподаватель",
                    values = state.curators,
                    itemTitle = { "${it.lastName} ${it.firstName} ${it.secondName}" },
                    onItemSelection = { component.selectCurator(it) },
                    selectedItem = state.curator
                )
                Spacer(Modifier.height(8.dp))
                AnimatedVisibility(
                    visible = state.curator != null
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TitledDropdown(
                            title = "Группа проектов",
                            values = state.projectGroups,
                            itemTitle = { it.title },
                            onItemSelection = { component.selectProjectGroup(it) },
                            selectedItem = state.projectGroup
                        )
                        Spacer(Modifier.height(8.dp))
                        StyledButton(
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            text = "Создать",
                            onClick = {
                                component.submit()
                            }
                        )
                    }
                }
            }
        }
    }
}