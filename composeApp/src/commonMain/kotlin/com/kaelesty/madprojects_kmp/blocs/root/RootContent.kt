package com.kaelesty.madprojects_kmp.blocs.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects_kmp.blocs.auth.AuthContent
import com.kaelesty.madprojects_kmp.blocs.createProject.CreateProjectContent
import com.kaelesty.madprojects_kmp.blocs.profile.ProfileContent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectContent

@Composable
fun RootContent(
	component: RootComponent,
) {

	Children(
		stack = component.stack,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colors.background)
		,
		animation = stackAnimation(slide())
	) {
		when (val instance = it.instance) {
			is RootComponent.Child.Project -> ProjectContent(component = instance.component)
			is RootComponent.Child.Profile -> ProfileContent(component = instance.component)
			is RootComponent.Child.Auth -> AuthContent(component = instance.component)
			is RootComponent.Child.CreateProject -> CreateProjectContent(component = instance.component)
		}
	}
}