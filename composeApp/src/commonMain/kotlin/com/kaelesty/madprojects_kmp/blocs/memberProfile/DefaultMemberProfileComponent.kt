package com.kaelesty.madprojects_kmp.blocs.memberProfile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultMemberProfileComponent(
    private val storeFactory: MemberProfileStoreFactory,
    private val componentContext: ComponentContext,
    private val jwt: String,
    private val navigator: MemberProfileComponent.Navigator
): ComponentContext by componentContext, MemberProfileComponent {

    private val store = instanceKeeper.getStore {
        storeFactory.create(jwt)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<MemberProfileStore.State>
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