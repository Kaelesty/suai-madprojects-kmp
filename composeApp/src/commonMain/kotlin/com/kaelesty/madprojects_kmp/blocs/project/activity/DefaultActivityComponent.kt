package com.kaelesty.madprojects_kmp.blocs.project.activity

import com.arkivanov.decompose.ComponentContext

class DefaultActivityComponent(
	private val componentContext: ComponentContext
): ComponentContext by componentContext, ActivityComponent {
}