package com.kaelesty.madprojects_kmp.blocs.memberProfile.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kaelesty.domain.common.JwtTool
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultProfileComponent(
    private val storeFactory: ProfileStoreFactory,
    private val componentContext: ComponentContext,
    private val jwtTool: JwtTool,
    private val navigator: ProfileComponent.Navigator
): ComponentContext by componentContext, ProfileComponent {

    private val store = instanceKeeper.getStore {
        storeFactory.create(jwtTool.getJwt())
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