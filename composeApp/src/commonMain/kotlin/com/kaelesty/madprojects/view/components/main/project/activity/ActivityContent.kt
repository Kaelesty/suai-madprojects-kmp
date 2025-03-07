package com.kaelesty.madprojects.view.components.main.project.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.ui.uikit.cards.CardErrorText
import com.kaelesty.madprojects_kmp.ui.uikit.Loading
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.SimplifiedDropdown

@Composable
fun ActivityContent(
    component: ActivityComponent
) {

    val state_ by component.state.collectAsState()
    val state = state_

    var showActivityDialog by remember { mutableStateOf(false) }

    if (state == null) {
        Loading()
    } else {

        ActivityDialog(
            isShown = showActivityDialog,
            state = state,
            onDismiss = { showActivityDialog = false }
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            item {
                SprintsCard(state, component)
                Spacer(Modifier.height(8.dp))
            }

            item {
                GithubActivityCard(state, component)
                Spacer(Modifier.height(8.dp))
            }

            item {
                StoryCard(
                    state = state,
                    onShowMore = {
                        showActivityDialog = true
                        component.loadFullActivity()
                    }
                )
            }
        }
    }
}

@Composable
private fun SprintsCard(
    state: ActivityComponent.State,
    component: ActivityComponent,
) {
    var isSprintsExpanded by remember { mutableStateOf(false) }
    TitledRoundedCard(
        title = "Спринты",
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val sprints = state.sprints
        if (sprints == null) {
            CardErrorText("Ошибка загрузки спринтов")
        } else {
            if (sprints.isEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Здесь пока пусто...",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                        ),
                        modifier = Modifier
                    )
                    StyledButton(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        text = "Начать спринт",
                        onClick = {
                            component.toSprintCreation()
                        }
                    )
                }
            } else {
                StyledList(
                    items = if (isSprintsExpanded) sprints else sprints.take(4),
                    itemTitle = { it.getTitleWithTime() },
                    onItemClick = { component.toSprint(it.id) },
                    leadingItem = "Начать спринт...",
                    onLeadingClick = { component.toSprintCreation() },
                    onDeleteItem = null,
                    trailingItem = if (sprints.size <= 4) null else if (isSprintsExpanded) "Свернуть" else "Еще",
                    onTrailingClick = { isSprintsExpanded = !isSprintsExpanded }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun GithubActivityCard(
    state: ActivityComponent.State,
    component: ActivityComponent,
) {
    TitledRoundedCard(
        title = "Активность в Github",
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val repoBranches = state.repoBranches
        if (repoBranches == null) {
            CardErrorText("Ошибка загрузки активности")
        } else {
            if (repoBranches.isEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Здесь пока пусто...",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                        ),
                        modifier = Modifier
                    )
                    Text(
                        text = "Чтобы добавить репозиторий, перейдите в настройки",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                        ),
                        modifier = Modifier
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    when (val commitsInstance = state.commits) {
                        CommitsState.Error -> {
                            Text(
                                text = "Здесь пока нет коммитов...",
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 18.sp,
                                    lineHeight = 24.sp,
                                ),
                                modifier = Modifier
                            )
                        }

                        CommitsState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(256.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is CommitsState.Main -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                SimplifiedDropdown(
                                    selectedItem = state.selectedYear,
                                    values = state.years,
                                    modifier = Modifier
                                        .weight(0.3f),
                                    onItemSelection = { component.setYear(it) },
                                    closeOnSelect = true,
                                    fontSize = 18.sp,
                                    itemTitle = { it.toString() }
                                )
                                val months = ActivityComponent.State.Month.entries.toList()
                                SimplifiedDropdown(
                                    selectedItem = state.selectedMonth,
                                    values = months,
                                    modifier = Modifier
                                        .weight(0.3f),
                                    onItemSelection = { component.setMonth(it) },
                                    closeOnSelect = true,
                                    fontSize = 18.sp,
                                    itemTitle = {
                                        it.string
                                    }
                                )
                            }
                            CalendarView(
                                branchCommits = commitsInstance.value.commits,
                                month = state.selectedMonth,
                                year = state.selectedYear,
                            )
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ветка:",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontSize = 18.sp,
                                        lineHeight = 24.sp,
                                    ),
                                    modifier = Modifier
                                        .weight(0.4f)
                                )
                                SimplifiedDropdown(
                                    selectedItem = state.selectedBranch,
                                    values = state.repoBranches,
                                    modifier = Modifier
                                        .weight(0.8f),
                                    onItemSelection = { component.selectBranch(it) },
                                    closeOnSelect = true,
                                    fontSize = 18.sp,
                                    itemTitle = { it.name }
                                )
                            }
                        }

                        CommitsState.Null -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryCard(
    state: ActivityComponent.State,
    onShowMore: () -> Unit,
) {
    TitledRoundedCard(
        title = "История",
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val activity = state.activity
        if (activity == null) {
            CardErrorText("Ошибка загрузки активности")
        }
        else if (activity.activities.isEmpty()) {
            Text(
                text = "Здесь пока пусто...",
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            )
        }
        else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                activity.activities.reversed()
                    .take(6)
                    .forEach {
                        ActivityView(
                            activity = it,
                            actor = activity.actors[it.actorId]
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                if (activity.activities.size > 6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    ) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "еще...",
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 24.sp,
                            ),
                            modifier = Modifier
                                .clickable { onShowMore() }
                        )
                    }
                }
            }
        }
    }
}