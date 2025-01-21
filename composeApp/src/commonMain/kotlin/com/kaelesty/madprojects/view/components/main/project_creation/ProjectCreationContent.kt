package com.kaelesty.madprojects.view.components.main.project_creation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.ui.uikit.StyledList
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField

@Composable
fun ProjectCreationContent(
    component: ProjectCreationComponent
) {

    val state by component.state.collectAsState()

    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = "Создание проекта"
    ) {
        StyledCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            TitledTextField(
                text = state.title,
                title = "Название",
                onValueChange = {
                    component.updateTitle(it)
                }
            )
            TitledTextField(
                text = state.desc,
                title = "Описание",
                onValueChange = {
                    component.updateDesc(it)
                }
            )
            TitledTextField(
                text = state.maxMembersCount.toString(),
                title = "Число учасников",
                onValueChange = {
                    component.updateMaxMembersCount(it.toInt())
                }
            )
        }
    }
}