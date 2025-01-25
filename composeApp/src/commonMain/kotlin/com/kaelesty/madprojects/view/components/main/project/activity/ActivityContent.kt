package com.kaelesty.madprojects.view.components.main.project.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.view.ui.uikit.cards.CardErrorText
import com.kaelesty.madprojects.view.ui.uikit.cards.CardText
import com.kaelesty.madprojects_kmp.ui.uikit.Loading
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.buttons.StyledButton
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard

@Composable
fun ActivityContent(
    component: ActivityComponent
) {

    val state_ by component.state.collectAsState()
    val state = state_

    if (state == null) {
        Loading()
    }
    else {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            SprintsCard(state, component)
            Spacer(Modifier.height(8.dp))
            GithubActivityCard(state, component)
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
                    Text(
                        text = "Чтобы добавить репозиторий, перейдите в настройки",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                        ),
                        modifier = Modifier
                    )
                }
            }
            else {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    repoBranches.forEach { repo ->
                        repo.repoBranches.forEach {
                            Text(
                                text = "${repo.name}/${it.name}",
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 20.sp,
                                    lineHeight = 24.sp,
                                ),
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}