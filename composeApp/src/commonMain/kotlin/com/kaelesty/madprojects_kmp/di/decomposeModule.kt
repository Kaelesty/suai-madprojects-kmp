package com.kaelesty.madprojects_kmp.di

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.auth.DefaultAuthComponent
import com.kaelesty.madprojects_kmp.blocs.login.DefaultLoginComponent
import com.kaelesty.madprojects_kmp.blocs.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.register.DefaultRegisterComponent
import com.kaelesty.madprojects_kmp.blocs.register.RegisterComponent
import com.kaelesty.madprojects_kmp.blocs.welcome.DefaultWelcomeComponent
import com.kaelesty.madprojects_kmp.blocs.welcome.WelcomeComponent
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
		(componentContext: ComponentContext) ->
		DefaultAuthComponent(
			componentContext = componentContext
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
}