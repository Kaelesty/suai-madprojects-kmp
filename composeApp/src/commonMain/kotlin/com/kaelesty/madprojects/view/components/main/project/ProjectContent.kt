package com.kaelesty.madprojects.view.components.main.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects.view.components.main.project.activity.ActivityContent
import com.kaelesty.madprojects.view.components.main.project.kanban.KanbanContent
import com.kaelesty.madprojects.view.components.main.project.sprint.SprintContent
import com.kaelesty.madprojects.view.components.main.project.sprint_creation.SprintCreationContent
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoContent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerContent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsContent

@Composable
fun ProjectContent(
    component: ProjectComponent
) {

    var selected by remember {
        mutableStateOf<ProjectComponent.Child.NavTarget?>(ProjectComponent.Child.NavTarget.Activity)
    }

    var showBottomBar by remember { mutableStateOf(true) }

    val state by component.state.collectAsState()
    Styled.uiKit().DefaultScreenScaffold(
        topBarTitle = state.projectName,
        isScrollable = true,
        bottomBar = {
            AnimatedVisibility(
                visible = selected != null && showBottomBar,
                enter = slideInVertically(
                    animationSpec = tween(200),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(200),
                    targetOffsetY = { it }
                )
            ) {
                ProjectBottomBar(
                    component = component,
                    selected = selected
                )
            }
        }
    ) {
        Children(
            stack = component.stack,
            animation = stackAnimation(slide())
        ) {
            when (val instance = it.instance) {
                is ProjectComponent.Child.Activity -> {
                    selected = ProjectComponent.Child.NavTarget.Activity
                    ActivityContent(instance.component)
                }

                is ProjectComponent.Child.Info -> {
                    selected = ProjectComponent.Child.NavTarget.Info
                    InfoContent(instance.component)
                }

                is ProjectComponent.Child.Kanban -> {
                    selected = ProjectComponent.Child.NavTarget.Kanban
                    KanbanContent(instance.component)
                }

                is ProjectComponent.Child.Messenger -> {
                    selected = ProjectComponent.Child.NavTarget.Messenger
                    MessengerContent(
                        instance.component,
                        onChatShown = {
                            showBottomBar = it
                        }
                    )
                }

                is ProjectComponent.Child.Settings -> {
                    selected = ProjectComponent.Child.NavTarget.Settings
                    SettingsContent(instance.component)
                }

                is ProjectComponent.Child.Sprint -> {
                    selected = null
                    SprintContent(instance.component)
                }

                is ProjectComponent.Child.SprintCreation -> {
                    selected = null
                    SprintCreationContent(instance.component)
                }
            }
        }
    }
}