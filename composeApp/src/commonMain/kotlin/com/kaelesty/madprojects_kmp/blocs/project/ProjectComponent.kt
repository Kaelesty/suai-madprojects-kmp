package com.kaelesty.madprojects_kmp.blocs.project

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent

interface ProjectComponent {

	val stack: Value<ChildStack<*, Child>>

	val state: ProjectStore.State

	sealed interface Child {
		class Messenger(val component: MessengerComponent): Child
		class Activity(val component: ActivityComponent): Child
		class Info(val component: InfoComponent): Child
		class Settings(val component: SettingsComponent): Child
		class Kanban(val component: KanbanComponent): Child

		enum class NavTarget {
			Activity, Messenger, Kanban, Info,  Settings
		}
	}

	fun navigate(child: Child.NavTarget)

	companion object {

		fun ChildToNavTarget(child: Child): Child.NavTarget = when(child) {
			is Child.Activity -> Child.NavTarget.Activity
			is Child.Info -> Child.NavTarget.Info
			is Child.Kanban -> Child.NavTarget.Kanban
			is Child.Messenger -> Child.NavTarget.Messenger
			is Child.Settings -> Child.NavTarget.Settings
		}
	}
}