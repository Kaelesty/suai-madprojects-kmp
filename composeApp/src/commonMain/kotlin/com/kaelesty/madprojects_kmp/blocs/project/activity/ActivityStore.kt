package com.kaelesty.madprojects_kmp.blocs.project.activity

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityStore.Intent
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityStore.Label
import com.kaelesty.madprojects_kmp.blocs.project.activity.ActivityStore.State

internal interface ActivityStore : Store<Intent, State, Label> {

    sealed interface Intent {

		data class OpenActivityLink(
			val linkTargetId: Int,
			val linkTargetType: State.ActivityEvent.LinkTargetType,
		): Intent

		data class OpenSprint(
			val sprintId: Int,
		): Intent
    }

    data class State(
		val activityList: List<ActivityEvent> = listOf(),
		val sprintList: List<Sprint> = listOf(),
	) {
		data class ActivityEvent(
			val id: Int,
			val timestamp: String,
			val text: String,
			val linkTargetId: Int,
			val linkTargetType: LinkTargetType,
			val linkText: String,
		) {

			enum class LinkTargetType {
				Sprint, Repository, Kard,
			}
		}

		data class Sprint(
			val id: Int,
			val test: String,
			val endDate: String?, // null == current
		)
	}

    sealed interface Label {

    }
}

internal class ActivityStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): ActivityStore =
        object : ActivityStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ActivityStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ActivityStoreFactory::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
			when (intent) {
				is Intent.OpenActivityLink -> TODO()
				is Intent.OpenSprint -> TODO()
			}
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            this
    }
}
