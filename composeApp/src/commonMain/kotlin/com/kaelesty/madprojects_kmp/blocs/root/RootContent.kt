package com.kaelesty.madprojects_kmp.blocs.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.kaelesty.madprojects_kmp.blocs.auth.AuthContent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectContent
import com.kaelesty.madprojects_kmp.ui.lock.Lock

@Composable
fun RootContent(
	component: RootComponent,
	lock: Lock
) {

	val lockState by lock.state.collectAsState()

	lockState?.let {
		it()
	}

	Children(
		stack = component.stack,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colors.background)
		,
		animation = stackAnimation(slide())
	) {
		when (val instance = it.instance) {
			is RootComponent.Child.Auth -> AuthContent(component = instance.component)
			is RootComponent.Child.Project -> ProjectContent(component = instance.component)
		}
	}
}