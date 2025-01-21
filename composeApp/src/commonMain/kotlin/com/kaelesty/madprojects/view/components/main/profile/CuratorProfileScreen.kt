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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.stores.ProfileStore
import com.kaelesty.madprojects.domain.repos.project.ProjectGroup
import com.kaelesty.madprojects.view.components.shared.elements.Avatar
import com.kaelesty.madprojects.view.components.shared.elements.TopBar
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard

@Composable
fun CuratorProfileScreen(
    state: ProfileStore.State.CuratorProfile
) {
    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = "Профиль"
    ) {
        CuratorMetaCard(state)
        Spacer(Modifier.height(18.dp))
        ProjectGroupsList(
            state = state,
            onProjectGroupClick = {},
            onCreateNewProjectGroup = {}
        )
    }
}

@Composable
fun ProjectGroupsList(
    state: ProfileStore.State.CuratorProfile,
    onProjectGroupClick: (ProjectGroup) -> Unit,
    onCreateNewProjectGroup: () -> Unit,
) {
    TitledRoundedCard(
        title = "Мои группы проектов",
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        StyledList(
            items = state.profile.projectGroups,
            itemTitle = { it.title },
            onItemClick = onProjectGroupClick,
            leadingItem = "Создать новую...",
            onLeadingClick = onCreateNewProjectGroup,
            onDeleteItem = null
        )
    }
}

@Composable
fun CuratorMetaCard(
    state: ProfileStore.State.CuratorProfile,
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
                        fontSize = 30.sp
                    ),
                text = "${state.profile.lastName} ${state.profile.firstName} ${state.profile.secondName}"
            )
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                listOf(
                    "Степень:" to state.profile.grade,
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