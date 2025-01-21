package com.kaelesty.madprojects.view.components

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.auth.User
import com.kaelesty.madprojects.view.components.auth.AuthComponent
import com.kaelesty.madprojects.view.components.auth.DefaultAuthComponent
import com.kaelesty.madprojects.view.components.main.DefaultMainComponent
import com.kaelesty.madprojects.view.components.main.MainComponent
import com.kaelesty.madprojects.view.components.main.profile.DefaultProfileComponent
import com.kaelesty.madprojects.view.components.main.profile.ProfileComponent
import com.kaelesty.madprojects.view.components.main.project.DefaultProjectComponent
import com.kaelesty.madprojects.view.components.main.project.ProjectComponent
import com.kaelesty.madprojects.view.components.main.project_creation.DefaultProjectCreationComponent
import com.kaelesty.madprojects.view.components.main.project_creation.ProjectCreationComponent
import com.kaelesty.madprojects.view.components.root.DefaultRootComponent
import com.kaelesty.madprojects.view.components.root.RootComponent
import com.kaelesty.madprojects_kmp.blocs.auth.login.DefaultLoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.login.LoginComponent
import com.kaelesty.madprojects_kmp.blocs.auth.register.DefaultRegisterComponent
import com.kaelesty.madprojects_kmp.blocs.auth.register.RegisterComponent
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

    factory<RegisterComponent.Factory> {
        object : RegisterComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: RegisterComponent.Navigator
            ): RegisterComponent {
                return DefaultRegisterComponent(
                    c, n,
                    storeFactory = get()
                )
            }
        }
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
                    registerComponentFactory = get(),
                )
            }
        }
    }

    factory<MainComponent.Factory> {
        object : MainComponent.Factory {
            override fun create(c: ComponentContext, user: User): MainComponent {
                return DefaultMainComponent(
                    c,
                    user = user,
                    profileComponentFactory = get(),
                    projectCreationComponentFactory = get(),
                    projectComponentFactory = get(),
                )
            }
        }
    }

    factory<ProfileComponent.Factory> {
        object : ProfileComponent.Factory {
            override fun create(c: ComponentContext, n: ProfileComponent.Navigator): ProfileComponent {
                return DefaultProfileComponent(
                    c,
                    n,
                    storeFactory = get()
                )
            }
        }
    }

    factory<ProjectCreationComponent.Factory> {
        object : ProjectCreationComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: ProjectCreationComponent.Navigator
            ): ProjectCreationComponent {
                return DefaultProjectCreationComponent(
                    c, n,
                    storeFactory = get()
                )
            }
        }
    }

    factory<ProjectComponent.Factory> {
        object : ProjectComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: ProjectComponent.Navigator,
                projectId: String
            ): ProjectComponent {
                return DefaultProjectComponent(
                    c, n,
                    projectId
                )
            }
        }
    }
}