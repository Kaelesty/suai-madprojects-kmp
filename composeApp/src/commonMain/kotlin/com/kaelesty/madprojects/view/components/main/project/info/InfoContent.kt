package com.kaelesty.madprojects_kmp.blocs.project.info

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.madprojects.view.ui.uikit.cards.CardErrorText
import com.kaelesty.madprojects_kmp.ui.theme.AppTheme
import com.kaelesty.madprojects_kmp.ui.uikit.Loading
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.cards.TitledRoundedCard
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun InfoContent(
	component: InfoComponent
) {

	val state by component.state.collectAsState()

	if (state.isLoading) {
		Loading()
	}
	else {
		val projectInstance = state.project
		if (projectInstance == null) {
			StyledRoundedCard(
				modifier = Modifier
					.fillMaxWidth()
					.height(120.dp)
			) {
				CardErrorText("Ошибка загрузки проекта")
			}
		}
		else {
			LazyColumn(
				modifier = Modifier
					.padding(horizontal = 4.dp)
			) {
				item {
					TitledRoundedCard(
						title = projectInstance.meta.title
					) {
						Text(
							text = projectInstance.meta.desc,
							modifier = Modifier.padding(12.dp)
						)
					}
				}
			}
		}

	}
}

