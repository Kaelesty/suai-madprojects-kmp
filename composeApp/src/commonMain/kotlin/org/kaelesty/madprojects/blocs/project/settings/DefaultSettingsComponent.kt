package com.kaelesty.madprojects_kmp.blocs.project.settings

import com.arkivanov.decompose.ComponentContext

class DefaultSettingsComponent(
	private val componentContext: ComponentContext
): ComponentContext by componentContext, SettingsComponent {
}