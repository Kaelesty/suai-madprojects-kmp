package com.kaelesty.madprojects_kmp.blocs.createProject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.blocs.profile.composables.CuratorsCard
import com.kaelesty.madprojects_kmp.blocs.profile.composables.MetaCard
import com.kaelesty.madprojects_kmp.blocs.profile.composables.ReposCard
import com.kaelesty.madprojects_kmp.ui.uikit.layout.TopBar

@Composable
fun CreateProjectContent(
    component: CreateProjectComponent
) {

    val state by component.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar("Создание проекта")
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(4.dp)
        ) {
            MetaCard(state, component)
            CuratorsCard(state, component)
            ReposCard(state, component)
        }
    }
}