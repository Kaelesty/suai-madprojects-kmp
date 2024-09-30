package com.kaelesty.madprojects_kmp.di

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects_kmp.decompose.auth.AuthComponent
import com.kaelesty.madprojects_kmp.decompose.auth.DefaultAuthComponent
import com.kaelesty.madprojects_kmp.decompose.auth.login.DefaultLoginComponent
import com.kaelesty.madprojects_kmp.decompose.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.decompose.auth.welcome.DefaultWelcomeComponent
import com.kaelesty.madprojects_kmp.decompose.auth.welcome.WelcomeComponent
import com.kaelesty.madprojects_kmp.decompose.root.DefaultRootComponent
import com.kaelesty.madprojects_kmp.decompose.root.RootComponent
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
		(componentContext: ComponentContext, func: () -> Unit) ->
		DefaultLoginComponent(
			componentContext = componentContext,
			storeFactory = get(),
			onSuccessfulAuth = func
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


}