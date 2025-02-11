package com.kaelesty.madprojects.view.components.main.project.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kaelesty.madprojects.view.ui.uikit.cards.CardErrorText
import com.kaelesty.madprojects_kmp.ui.uikit.Loading
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard

@Composable
fun ActivityDialog(
    isShown: Boolean,
    state: ActivityComponent.State,
    onDismiss: () -> Unit,
) {
    if (!isShown) return
    Dialog(
        onDismiss
    ) {
        StyledRoundedCard {
            if (state.isLoadingActivity) {
                Box(
                    modifier = Modifier
                        .size(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else {
                val activity = state.activity
                if (activity == null) {
                    CardErrorText("Ошибка загрузки активности")
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(activity.activities.reversed()) {
                            activity.actors[it.actorId]?.let { actor ->
                                ActivityView(
                                    activity = it,
                                    actor = actor
                                )
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }

                }
            }
        }
    }
}