package com.kaelesty.madprojects_kmp.blocs.memberProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.blocs.project.bottomBorder
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chat.Avatar
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.layout.TopBar
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.right_arrow
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MemberProfileContent(
    component: MemberProfileComponent
) {

    val state by component.state.collectAsState()
    val gradientColors = listOf(
        MaterialTheme.colors.secondary,
        MaterialTheme.colors.secondaryVariant,
        MaterialTheme.colors.onSecondary
    )

    Scaffold(
        topBar = {
            TopBar("Профиль")
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(4.dp)
        ) {
            MetaCard(state, gradientColors)
            Spacer(Modifier.height(18.dp))
            ProjectsList(state, gradientColors, component)
        }
    }
}

@Composable
private fun ProjectsList(
    state: MemberProfileStore.State,
    gradientColors: List<Color>,
    component: MemberProfileComponent
) {
    TitledRoundedCard(
        title = "Мои проекты",
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
        ) {
            state.projects.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bottomBorder(
                            brush = Brush.linearGradient(
                                gradientColors,
                                tileMode = TileMode.Decal
                            ),
                            height = 2f
                        )
                        .clickable {
                            component.onOpenProject(it.id)
                        }
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                        ),
                        modifier = Modifier
                            .weight(1f)
                    )
                    Icon(
                        vectorResource(Res.drawable.right_arrow),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetaCard(
    state: MemberProfileStore.State,
    gradientColors: List<Color>
) {
    StyledRoundedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            Avatar(
                modifier = Modifier.size(128.dp),
                url = state.avatarUrl,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.body2
                    .copy(
                        fontSize = 30.sp
                    ),
                text = state.userName
            )
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                listOf(
                    "Группа:" to state.group,
                    "Github:" to state.githubLink,
                    "Email:" to state.email
                ).forEach {
                    Row {
                        Text(
                            text = it.first,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 24.sp
                            )
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
                                        gradientColors,
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