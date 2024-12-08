package com.kaelesty.madprojects_kmp.blocs.profile.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectComponent
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard

@Composable
fun MetaCard(
    state: CreateProjectStore.State,
    component: CreateProjectComponent
) {
    TitledRoundedCard(
        title = "О проекте",
    ) {
        Row {  }
    }
}