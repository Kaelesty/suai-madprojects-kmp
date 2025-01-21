package com.kaelesty.madprojects.view.components.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects.view.components.main.profile.CommonProfileContent
import com.kaelesty.madprojects.view.components.main.project.ProjectContent
import com.kaelesty.madprojects.view.components.main.project_creation.ProjectCreationContent

@Composable
fun MainContent(
    component: MainComponent
) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) {
        when (val instance = it.instance) {
            is MainComponent.Child.Profile -> CommonProfileContent(instance.component)
            is MainComponent.Child.Project -> ProjectContent(instance.component)
            is MainComponent.Child.ProjectCreation -> ProjectCreationContent(instance.component)
        }
    }
}