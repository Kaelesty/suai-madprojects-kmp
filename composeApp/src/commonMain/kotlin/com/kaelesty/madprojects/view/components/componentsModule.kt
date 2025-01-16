package com.kaelesty.madprojects.view.components

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.view.components.auth.AuthComponent
import com.kaelesty.madprojects.view.components.auth.DefaultAuthComponent
import com.kaelesty.madprojects.view.components.main.DefaultMainComponent
import com.kaelesty.madprojects.view.components.main.MainComponent
import com.kaelesty.madprojects.view.components.root.DefaultRootComponent
import com.kaelesty.madprojects.view.components.root.RootComponent
import com.kaelesty.madprojects_kmp.blocs.auth.login.DefaultLoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.DefaultWelcomeComponent
import com.kaelesty.madprojects_kmp.blocs.auth.welcome.WelcomeComponent
import org.koin.dsl.module

val componentsModule = module {

    single<RootComponent> {
        DefaultRootComponent(
            componentContext = it.get(),
            storeFactory = get(),
            authComponentFactory = get(),
            mainComponentFactory = get(),
        )
    }

    factory<LoginComponent.Factory> {
        object: LoginComponent.Factory {
            override fun create(c: ComponentContext, n: LoginComponent.Navigator): LoginComponent {
                return DefaultLoginComponent(
                    c, n,
                    storeFactory = get(),
                )
            }
        }
    }

    factory<WelcomeComponent.Factory> {
        object: WelcomeComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: WelcomeComponent.Navigator
            ): WelcomeComponent {
                return DefaultWelcomeComponent(
                    c, n
                )
            }
        }
    }

    factory<AuthComponent.Factory> {
        object : AuthComponent.Factory {
            override fun create(c: ComponentContext): AuthComponent {
                return DefaultAuthComponent(
                    c,
                    welcomeComponentFactory = get(),
                    loginComponentFactory = get(),
                )
            }
        }
    }

    factory<MainComponent.Factory> {
        DefaultMainComponent.Companion.Factory
    }
}