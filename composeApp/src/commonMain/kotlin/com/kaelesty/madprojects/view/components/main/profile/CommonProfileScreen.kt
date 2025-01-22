package com.kaelesty.madprojects.view.components.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.repos.profile.ProfileProject
import com.kaelesty.madprojects.domain.stores.ProfileStore
import com.kaelesty.madprojects.view.components.shared.elements.Avatar
import com.kaelesty.madprojects.view.components.shared.elements.TopBar
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard


@Composable
fun CommonProfileScreen(
    state: ProfileStore.State.CommonProfile,
    onCreateNewProject: () -> Unit,
    onProjectClick: (ProfileProject) -> Unit
) {
    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = "Профиль"
    ) {
        MetaCard(state)
        Spacer(Modifier.height(18.dp))
        ProjectsList(
            state = state,
            onProjectClick = onProjectClick,
            onCreateNewProject = onCreateNewProject,
        )
    }
}

@Composable
fun ProjectsList(
    state: ProfileStore.State.CommonProfile,
    onProjectClick: (ProfileProject) -> Unit,
    onCreateNewProject: () -> Unit,
) {
    TitledRoundedCard(
        title = "Мои проекты",
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        StyledList(
            items = state.profile.projects,
            itemTitle = { it.title },
            onItemClick = onProjectClick,
            leadingItem = "Создать новый...",
            onLeadingClick = onCreateNewProject,
            onDeleteItem = null
        )
    }
}

@Composable
fun MetaCard(
    state: ProfileStore.State.CommonProfile,
) {
    StyledRoundedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO github
            Spacer(Modifier.height(8.dp))
            Avatar(
                modifier = Modifier.size(128.dp),
                url = state.profile.githubMeta?.githubAvatar,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.body2
                    .copy(
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    ),
                text = "${state.profile.lastName} ${state.profile.firstName} ${state.profile.secondName}",
                maxLines = 2
            )
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                listOf(
                    "Группа:" to state.profile.group,
                    "Email:" to state.profile.email
                ).forEach {
                    Row {
                        Text(
                            text = it.first,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 24.sp
                            ),
                            modifier = Modifier
                                .width(80.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = it.second,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic,
                            ),
                            modifier = Modifier
                                .bottomBorder(
                                    brush = Brush.linearGradient(
                                        Styled.uiKit().colors().gradient,
                                        tileMode = TileMode.Decal
                                    ),
                                    height = 4f
                                )
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}