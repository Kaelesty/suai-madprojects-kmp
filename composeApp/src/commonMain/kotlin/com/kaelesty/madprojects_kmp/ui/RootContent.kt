package com.kaelesty.madprojects_kmp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.kaelesty.madprojects_kmp.decompose.root.RootComponent

@Composable
fun RootContent(
	component: RootComponent
) {
	Children(
		stack = component.stack,
		modifier = Modifier
			.fillMaxSize()
	) {
		when (val instance = it.instance) {
			is RootComponent.Child.Auth -> AuthContent(component = instance.component)
		}
	}
}