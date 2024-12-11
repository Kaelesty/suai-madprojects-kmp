package com.kaelesty.madprojects_kmp.blocs.createProject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects_kmp.blocs.createProject.composables.CuratorsCard
import com.kaelesty.madprojects_kmp.blocs.createProject.composables.MetaCard
import com.kaelesty.madprojects_kmp.blocs.createProject.composables.ReposCard
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
                .fillMaxSize()
                .padding(it)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MetaCard(state, component)
            CuratorsCard(state, component)
            ReposCard(state, component)
        }
    }
}