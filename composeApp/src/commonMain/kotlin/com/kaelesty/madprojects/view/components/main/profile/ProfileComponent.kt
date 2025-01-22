package com.kaelesty.madprojects.view.components.main.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects.domain.stores.ProfileStore
import com.kaelesty.madprojects.domain.stores.ProfileStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val state: StateFlow<ProfileStore.State>

    fun toProjectCreation()
    
    interface Navigator {
        
        fun toProjectCreation()

    }

    interface Factory{

        fun create(
            c: ComponentContext,
            n: Navigator
        ): ProfileComponent
    }

}

class DefaultProfileComponent(
    private val componentContext: ComponentContext,
    private val navigator: ProfileComponent.Navigator,
    private val storeFactory: ProfileStoreFactory,
): ProfileComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun toProjectCreation() {
        navigator.toProjectCreation()
    }
}