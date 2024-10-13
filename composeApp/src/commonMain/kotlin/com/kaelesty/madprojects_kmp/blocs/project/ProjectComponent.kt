package com.kaelesty.madprojects_kmp.blocs.project

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.activity_nav
import madprojects.composeapp.generated.resources.info_nav
import madprojects.composeapp.generated.resources.kanban_nav
import madprojects.composeapp.generated.resources.messenger_nav
import madprojects.composeapp.generated.resources.settings_nav

import org.jetbrains.compose.resources.DrawableResource

interface ProjectComponent {

	val stack: Value<ChildStack<*, Child>>

	val state: ProjectStore.State

	sealed interface Child {
		class Messenger(val component: MessengerComponent): Child
		class Activity(val component: ActivityComponent): Child
		class Info(val component: InfoComponent): Child
		class Settings(val component: SettingsComponent): Child
		class Kanban(val component: KanbanComponent): Child

		enum class NavTarget(
			val title: String,
			val icon: DrawableResource,
			val iconScale: Float
		) {
			Activity("Активность", Res.drawable.activity_nav, 0.9f),
			Messenger("Мессенджер", Res.drawable.messenger_nav, 0.85f),
			Info("Информация", Res.drawable.info_nav, 0.85f),
			Kanban("Канбан", Res.drawable.kanban_nav, 0.85f),
			Settings("Настройки", Res.drawable.settings_nav, 0.9f),
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