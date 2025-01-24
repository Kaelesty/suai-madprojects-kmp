package com.kaelesty.madprojects.view.components.main.project_creation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects.view.ui.uikit.dialogues.StyledCreationDialog
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.TitledDropdown
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField

@Composable
fun ProjectCreationContent(
    component: ProjectCreationComponent
) {

    val state by component.state.collectAsState()
    var isRepolinkDialogVisible by remember { mutableStateOf(false) }
    var repolinkDialogError by remember { mutableStateOf<String?>(null) }
    val repoLinkState by component.repoLinkState.collectAsState()

    LaunchedEffect(repoLinkState) {
        when (repoLinkState) {
            ProjectCreationComponent.RepoLinkState.Error -> {
                repolinkDialogError = "Ошибка добавления"
            }

            ProjectCreationComponent.RepoLinkState.Finished -> {
                repolinkDialogError = ""
                isRepolinkDialogVisible = false
            }

            else -> {}
        }
    }

    StyledCreationDialog(
        isVisible = isRepolinkDialogVisible,
        title = "Введите ссылку",
        actionTitle = "Добавить",
        onAction = {
            component.addRepolink(it)
        },
        onHide = { isRepolinkDialogVisible = false },
        error = repolinkDialogError

    )

    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = "Создание проекта",
        isScrollable = true,
        bottomBar = {}
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
                Text(
                    text = "Репозитории",
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 22.sp
                    )
                )
                StyledList(
                    items = state.repoLinks
                        .map {
                            it.split("/").lastOrNull() ?: it
                        },
                    itemTitle = { it },
                    leadingItem = "Добавить",
                    onLeadingClick = { isRepolinkDialogVisible = true },
                    onDeleteItem = { component.removeRepoLink(it) },
                    onItemClick = null
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