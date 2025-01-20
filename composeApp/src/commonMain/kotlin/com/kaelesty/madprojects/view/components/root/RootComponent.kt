package com.kaelesty.madprojects.view.components.root

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects.domain.stores.root.RootStore
import com.kaelesty.madprojects.domain.stores.root.RootStoreFactory
import com.kaelesty.madprojects.domain.stores.root.User
import com.kaelesty.madprojects.view.components.auth.AuthComponent
import com.kaelesty.madprojects.view.components.main.MainComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        class Auth(val component: AuthComponent): Child

        class Main(val component: MainComponent): Child
    }
}

class DefaultRootComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: RootStoreFactory,
    private val authComponentFactory: AuthComponent.Factory,
    private val mainComponentFactory: MainComponent.Factory
): RootComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
            .apply {
                scope.launch {
                    stateFlow.collect {
                        onState(it)
                    }
                }
            }
    }

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Auth,
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    private fun onState(state: RootStore.RootState) = when (state) {
        is RootStore.RootState.Authorized -> navigation.replaceAll(
            Config.Main(
                user = state.user
            )
        )
        RootStore.RootState.Unauthorized -> navigation.replaceAll(
            Config.Auth
        )
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when(config) {
        Config.Auth -> RootComponent.Child.Auth(
            component = authComponentFactory.create(componentContext)
        )
        is Config.Main -> RootComponent.Child.Main(
            component = mainComponentFactory.create(componentContext, config.user)
        )
    }

    @Serializable
    private sealed interface Config {

        data object Auth: Config

        data class Main(val user: User): Config
    }
}