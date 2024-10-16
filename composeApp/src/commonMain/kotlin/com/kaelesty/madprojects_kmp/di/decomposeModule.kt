package com.kaelesty.madprojects_kmp.di

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityComponent
import com.kaelesty.madprojects_kmp.blocs.project.activity.DefaultActivityComponent
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.auth.DefaultAuthComponent
import com.kaelesty.madprojects_kmp.blocs.auth.login.DefaultLoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.project.DefaultProjectComponent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectComponent
import com.kaelesty.madprojects_kmp.blocs.auth.register.DefaultRegisterComponent
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterComponent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.DefaultWelcomeComponent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.WelcomeComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.DefaultInfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.DefaultKanbanComponent
import com.kaelesty.madprojects_kmp.blocs.project.kanban.KanbanComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.DefaultMessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.ChatsListComponent
import com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist.DefaultChatListComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.DefaultSettingsComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent
import com.kaelesty.madprojects_kmp.blocs.root.DefaultRootComponent
import com.kaelesty.madprojects_kmp.blocs.root.RootComponent
import org.koin.dsl.module

val decomposeModule = module {

	factory<RootComponent> {
		(componentContext: ComponentContext) ->
		DefaultRootComponent(
			componentContext = componentContext
		)
	}

	factory<AuthComponent> {
		(componentContext: ComponentContext, navigator: AuthComponent.Navigator) ->
		DefaultAuthComponent(
			componentContext = componentContext,
			navigator = navigator,
		)
	}

	factory<LoginComponent> {
		(componentContext: ComponentContext, navigator: LoginComponent.Navigator) ->
		DefaultLoginComponent(
			componentContext = componentContext,
			storeFactory = get(),
			navigator = navigator
		)
	}

	factory<WelcomeComponent> {
		(
			componentContext: ComponentContext,
			navigator: WelcomeComponent.Navigator,
		) ->
			DefaultWelcomeComponent(
				componentContext = componentContext,
				navigator = navigator,
			)
	}

	factory<RegisterComponent> {
		(componentContext: ComponentContext, navigator: RegisterComponent.Navigator) ->
		DefaultRegisterComponent(
			componentContext = componentContext,
			navigator = navigator,
			storeFactory = get(),
		)
	}

	factory<ProjectComponent> {
		(componentContext: ComponentContext) ->
		DefaultProjectComponent(
			componentContext = componentContext,
			storeFactory = get(),
			projectId = 0
		)
	}

	factory<ActivityComponent> {
		(componentContext: ComponentContext) ->
		DefaultActivityComponent(
			componentContext = componentContext,
			storeFactory = get()
		)
	}

	factory<InfoComponent> {
			(componentContext: ComponentContext) ->
		DefaultInfoComponent(
			componentContext = componentContext
		)
	}

	factory<KanbanComponent> {
			(componentContext: ComponentContext) ->
		DefaultKanbanComponent(
			componentContext = componentContext
		)
	}

	factory<MessengerComponent> {
			(componentContext: ComponentContext) ->
		DefaultMessengerComponent(
			componentContext = componentContext,
			storeFactory = get()
		)
	}

	factory<SettingsComponent> {
			(componentContext: ComponentContext) ->
		DefaultSettingsComponent(
			componentContext = componentContext
		)
	}

	factory<ChatsListComponent> {
			(componentContext: ComponentContext, store: MessengerStore) ->
		DefaultChatListComponent(
			componentContext = componentContext,
			store = store
		)
	}
}