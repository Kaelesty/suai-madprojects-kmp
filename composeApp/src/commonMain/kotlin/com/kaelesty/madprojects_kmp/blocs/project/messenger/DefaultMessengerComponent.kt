package com.kaelesty.madprojects_kmp.blocs.project.messenger

import com.arkivanov.decompose.ComponentContext

class DefaultMessengerComponent(
	private val componentContext: ComponentContext
): ComponentContext by componentContext, MessengerComponent {
}