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
): ComponentContext by componentContext, MemberProfileComponent {

    private val store = instanceKeeper.getStore {
        storeFactory.create(jwt)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<MemberProfileStore.State>
        get() = store.stateFlow

    override fun onOpenProject(projectId: Int) {
        store.accept(MemberProfileStore.Intent.OpenProject(projectId))
    }

    override fun onConnectProject() {
        store.accept(MemberProfileStore.Intent.ConnectProject)
    }

    override fun onCreateProject() {
        store.accept(MemberProfileStore.Intent.CreateProject)
    }

    override fun onEditProfile() {
        store.accept(MemberProfileStore.Intent.EditProfile)
    }
}