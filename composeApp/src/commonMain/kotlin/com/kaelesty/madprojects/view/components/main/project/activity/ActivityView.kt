package com.kaelesty.madprojects.view.components.main.project.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.repos.profile.SharedProfile
import com.kaelesty.madprojects.domain.repos.story.Activity
import com.kaelesty.madprojects.domain.repos.story.ActivityType
import com.kaelesty.madprojects.view.extensions.bottomBorder
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.extensions.toReadableTime

@Composable
fun ActivityView(
    activity: Activity,
    actor: SharedProfile
) {
    Box(
        modifier = Modifier
            .bottomBorder(
                brush = Brush.linearGradient(
                    Styled.uiKit().colors().gradient,
                    tileMode = TileMode.Decal
                ),
                height = 2f
            )
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontStyle = MaterialTheme.typography.body2.fontStyle,
                            fontWeight = FontWeight.W300
                        )
                    ) {
                        append("${actor.firstName} ${actor.lastName}")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontStyle = MaterialTheme.typography.body2.fontStyle
                        )
                    ) {
                        append(
                            when (activity.type) {
                                ActivityType.RepoBind -> " добавил репозиторий "
                                ActivityType.RepoUnbind -> " удалил реполиторий "
                                ActivityType.SprintStart -> " начал спринт "
                                ActivityType.SprintFinish -> " закончил спринт "
                                ActivityType.KardMove -> " переместил карточку "
                                ActivityType.MemberAdd -> " присоединился проекту"
                                ActivityType.MemberRemove -> " покинул проект"
                            }
                        )
                    }
                    withStyle(
                        style = SpanStyle(
                            fontStyle = MaterialTheme.typography.body2.fontStyle,
                            fontWeight = FontWeight.W300
                        )
                    ) {
                        append(
                            activity.targetTitle
                        )
                    }
                    if (activity.type == ActivityType.KardMove) {
                        withStyle(
                            style = SpanStyle(
                                fontStyle = MaterialTheme.typography.body2.fontStyle,
                            )
                        ) {
                            append(
                                " в столбец "
                            )
                        }
                        withStyle(
                            style = SpanStyle(
                                fontStyle = MaterialTheme.typography.body2.fontStyle,
                                fontWeight = FontWeight.W300
                            )
                        ) {
                            append(
                                activity.secondaryTargetTitle ?: "..."
                            )
                        }
                    }
                },
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 20.sp,
                ),
                modifier = Modifier
                    .weight(0.80f)
                ,
            )
            Text(
                text = activity.timeMillis.toReadableTime(),
                modifier = Modifier
                    .weight(0.2f)
                ,
            )
        }
    }
}