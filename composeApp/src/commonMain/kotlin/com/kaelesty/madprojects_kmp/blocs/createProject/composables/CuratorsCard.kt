package com.kaelesty.madprojects_kmp.blocs.createProject.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectComponent
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore
import com.kaelesty.madprojects_kmp.ui.uikit.Loading
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.dropdowns.StyledDropdown

@Composable
fun CuratorsCard(
    state: CreateProjectStore.State,
    component: CreateProjectComponent
) {

    TitledRoundedCard(
        title = "Преподаватель",
        modifier = Modifier
            .fillMaxWidth(0.95f)
    ) {
        Column(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(0.95f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.curators.isEmpty()) {
                Loading()
            } else {
                Spacer(Modifier.height(8.dp))
                StyledDropdown(
                    values = state.curators.map { it.name },
                    selectedIndex = state.curators.indexOf(state.selectedCurator),
                    onItemSelection = {
                        component.accept(
                            CreateProjectStore.Intent.UpdateCurator(
                                new = state.curators[it]
                            )
                        )
                    }
                )
            }
        }
    }
}