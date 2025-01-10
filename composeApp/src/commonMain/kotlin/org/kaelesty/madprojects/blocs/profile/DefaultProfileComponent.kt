package com.kaelesty.madprojects_kmp.blocs.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultProfileComponent(
    private val storeFactory: ProfileStoreFactory,
    private val componentContext: ComponentContext,
    private val navigator: ProfileComponent.Navigator
): ComponentContext by componentContext, ProfileComponent {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ProfileStore.State>
        get() = store.stateFlow

    override fun onOpenProject(projectId: Int) {
        navigator.openProject(projectId)
    }

    override fun onConnectProject() {
        navigator.connectProject()
    }

    override fun onCreateProject() {
        navigator.createProject()
    }

    override fun onEditProfile() {
        navigator.editProfile()
    }
}