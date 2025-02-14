package com.kaelesty.madprojects.view.components.main.other_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.UserType
import com.kaelesty.madprojects.domain.repos.profile.ProfileProject
import com.kaelesty.madprojects.domain.repos.profile.SharedProfileResponse
import com.kaelesty.madprojects.domain.stores.ProfileStore
import com.kaelesty.madprojects.view.components.main.profile.MetaCard
import com.kaelesty.madprojects.view.components.main.profile.ProjectsList
import com.kaelesty.madprojects.view.components.shared.elements.Avatar
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects.view.ui.uikit.cards.CardErrorText
import com.kaelesty.madprojects_kmp.ui.uikit.Loading
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard

@Composable
fun OtherProfileContent(
    component: OtherProfileComponent
) {

    val state by component.state.collectAsState()

    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = "Профиль",
        isScrollable = false,
        bottomBar = {}
    ) {
        if (state.isLoading) {
            Loading()
        }
        else OtherMetaCard(state.profile)
    }
}

@Composable
fun OtherMetaCard(
    state: SharedProfileResponse?,
) {

    if (state == null) {
        StyledRoundedCard(modifier = Modifier.height(80.dp)) {
            CardErrorText("Ошибка загрузки")
        }
    }
    else StyledRoundedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            Avatar(
                modifier = Modifier.size(128.dp),
                url = state.avatar,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                style = MaterialTheme.typography.body2
                    .copy(
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    ),
                text = "${state.lastName} ${state.firstName} ${state.secondName}",
                maxLines = 2
            )
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                val dataTitle = when (state.role) {
                    UserType.Common -> "Группа:"
                    UserType.Curator -> "Степень:"
                }
                listOf(
                    dataTitle to state.data,
                    "Email:" to state.email
                ).forEach {
                    Row {
                        Text(
                            text = it.first,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 20.sp
                            ),
                            modifier = Modifier
                                .width(80.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = it.second,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 20.sp,
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