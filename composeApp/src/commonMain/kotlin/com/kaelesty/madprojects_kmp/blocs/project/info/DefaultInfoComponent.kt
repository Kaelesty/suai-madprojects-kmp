package com.kaelesty.madprojects_kmp.blocs.project.info

import com.arkivanov.decompose.ComponentContext

class DefaultInfoComponent(
	private val componentContext: ComponentContext
): ComponentContext by componentContext, InfoComponent {
}