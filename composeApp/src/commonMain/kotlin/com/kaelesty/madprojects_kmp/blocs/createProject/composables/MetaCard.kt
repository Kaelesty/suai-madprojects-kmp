package com.kaelesty.madprojects_kmp.blocs.createProject.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectComponent
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectStore
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TinyTitledTextField
import com.kaelesty.madprojects_kmp.ui.uikit.text.TitledTextField

@Composable
fun MetaCard(
    state: CreateProjectStore.State,
    component: CreateProjectComponent
) {
    TitledRoundedCard(
        title = "О проекте",
        modifier = Modifier
            .fillMaxWidth(0.95f)
    ) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(0.95f)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
            ) {
                TinyTitledTextField(
                    text = state.name,
                    title = "Название",
                    onValueChange = {
                        component.accept(
                            CreateProjectStore.Intent.UpdateName(it)
                        )
                    },
                )
                Spacer(Modifier.width(8.dp))
                TinyTitledTextField(
                    text = state.membersCount.toString(),
                    title = "Участники",
                    onValueChange = {
                        component.accept(
                            CreateProjectStore.Intent.UpdateMembersCount(it)
                        )
                    },
                    keyboardType = KeyboardType.Number,
                )
            }
            Spacer(Modifier.height(16.dp))
            TinyTitledTextField(
                text = state.desc,
                title = "Описание проекта",
                onValueChange = {
                    component.accept(
                        CreateProjectStore.Intent.UpdateDesc(it)
                    )
                },
                maxLines = 16,
                height = 180.dp,
                modifier = Modifier.fillMaxWidth(0.95f)
            )
        }
    }
}