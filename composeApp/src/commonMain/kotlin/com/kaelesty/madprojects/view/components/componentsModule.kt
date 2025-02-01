package com.kaelesty.madprojects.view.components

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.auth.User
import com.kaelesty.madprojects.domain.repos.profile.ProfileProject
import com.kaelesty.madprojects.view.components.auth.AuthComponent
import com.kaelesty.madprojects.view.components.auth.DefaultAuthComponent
import com.kaelesty.madprojects.view.components.main.DefaultMainComponent
import com.kaelesty.madprojects.view.components.main.MainComponent
import com.kaelesty.madprojects.view.components.main.profile.DefaultProfileComponent
import com.kaelesty.madprojects.view.components.main.profile.ProfileComponent
import com.kaelesty.madprojects.view.components.main.project.DefaultProjectComponent
import com.kaelesty.madprojects.view.components.main.project.ProjectComponent
import com.kaelesty.madprojects.view.components.main.project.activity.ActivityComponent
import com.kaelesty.madprojects.view.components.main.project.activity.DefaultActivityComponent
import com.kaelesty.madprojects.view.components.main.project.kanban.DefaultKanbanComponent
import com.kaelesty.madprojects.view.components.main.project.kanban.KanbanComponent
import com.kaelesty.madprojects.view.components.main.project.messenger.DefaultMessengerComponent
import com.kaelesty.madprojects.view.components.main.project.messenger.MessengerComponent
import com.kaelesty.madprojects.view.components.main.project.sprint.DefaultSprintComponent
import com.kaelesty.madprojects.view.components.main.project.sprint.SprintComponent
import com.kaelesty.madprojects.view.components.main.project.sprint_creation.DefaultSprintCreationComponent
import com.kaelesty.madprojects.view.components.main.project.sprint_creation.SprintCreationComponent
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
import com.kaelesty.madprojects_kmp.blocs.project.info.DefaultInfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.info.InfoComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.DefaultSettingsComponent
import com.kaelesty.madprojects_kmp.blocs.project.settings.SettingsComponent
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
        object : LoginComponent.Factory {
            override fun create(c: ComponentContext, n: LoginComponent.Navigator): LoginComponent {
                return DefaultLoginComponent(
                    c, n,
                    storeFactory = get(),
                )
            }
        }
    }

    factory<WelcomeComponent.Factory> {
        object : WelcomeComponent.Factory {
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
            override fun create(
                c: ComponentContext,
                n: ProfileComponent.Navigator
            ): ProfileComponent {
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
                project: ProfileProject
            ): ProjectComponent {
                return DefaultProjectComponent(
                    c, n,
                    project,
                    activityComponentFactory = get(),
                    infoComponentFactory = get(),
                    kanbanComponentFactory = get(),
                    messengerComponentFactory = get(),
                    settingsComponentFactory = get(),
                    sprintCreationComponentFactory = get(),
                    sprintComponentFactory = get(),
                    socketRepository = get(),
                )
            }
        }
    }

    factory<MessengerComponent.Factory> {
        object : MessengerComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: MessengerComponent.Navigator,
                projectId: String,
            ): MessengerComponent {
                return DefaultMessengerComponent(
                    c, n, projectId,
                    sockerRepository = get()
                )
            }
        }
    }

    factory<InfoComponent.Factory> {
        object : InfoComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: InfoComponent.Navigator,
                projectId: String,
            ): InfoComponent {
                return DefaultInfoComponent(
                    c, n, projectId
                )
            }
        }
    }

    factory<SettingsComponent.Factory> {
        object : SettingsComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: SettingsComponent.Navigator,
                projectId: String,
            ): SettingsComponent {
                return DefaultSettingsComponent(
                    c, n, projectId
                )
            }
        }
    }

    factory<KanbanComponent.Factory> {
        object : KanbanComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: KanbanComponent.Navigator,
                projectId: String,
            ): KanbanComponent {
                return DefaultKanbanComponent(
                    c, n, projectId
                )
            }
        }
    }

    factory<ActivityComponent.Factory> {
        object : ActivityComponent.Factory {
            override fun create(
                c: ComponentContext,
                n: ActivityComponent.Navigator,
                projectId: String,
            ): ActivityComponent {
                return DefaultActivityComponent(
                    c, n, projectId,
                    sprintsRepo = get(),
                    githubRepo = get(),
                )
            }
        }
    }

    factory<SprintComponent.Factory> {
            object : SprintComponent.Factory {
                override fun create(
                    c: ComponentContext,
                    n: SprintComponent.Navigator,
                    projectId: String,
                    sprintId: String
                ): SprintComponent {
                    return DefaultSprintComponent(
                        c, n, projectId, sprintId
                    )
                }
            }
        }

    factory<SprintCreationComponent.Factory> {
            object : SprintCreationComponent.Factory {
                override fun create(
                    c: ComponentContext,
                    n: SprintCreationComponent.Navigator,
                    projectId: String
                ): SprintCreationComponent {
                    return DefaultSprintCreationComponent(
                        c, n, projectId
                    )
                }
            }
        }
}