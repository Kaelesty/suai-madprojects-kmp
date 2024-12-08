package com.kaelesty.madprojects_kmp.blocs.createProject

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultCreateProjectComponent(
    componentContext: ComponentContext,
    private val storeFactory: CreateProjectStoreFactory,
): CreateProjectComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    override val state: StateFlow<CreateProjectStore.State>
        get() = store.stateFlow

    override fun accept(intent: CreateProjectStore.Intent) {
        store.accept(intent)
    }
}