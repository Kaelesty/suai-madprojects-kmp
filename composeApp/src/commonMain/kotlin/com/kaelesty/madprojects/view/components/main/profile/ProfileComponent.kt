package com.kaelesty.madprojects.view.components.main.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.madprojects.domain.repos.profile.ProfileStore
import com.kaelesty.madprojects.domain.repos.profile.ProfileStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val state: StateFlow<ProfileStore.State>

    interface Factory{

        fun create(
            c: ComponentContext
        ): ProfileComponent
    }

}

class DefaultProfileComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: ProfileStoreFactory,
): ProfileComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow
}