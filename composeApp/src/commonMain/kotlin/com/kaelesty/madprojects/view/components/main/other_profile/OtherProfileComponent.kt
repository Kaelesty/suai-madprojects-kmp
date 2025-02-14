package com.kaelesty.madprojects.view.components.main.other_profile

import com.arkivanov.decompose.ComponentContext
import com.kaelesty.madprojects.domain.repos.profile.ProfileRepo
import com.kaelesty.madprojects.domain.repos.profile.SharedProfileResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface OtherProfileComponent {

    data class State(
        val isLoading: Boolean = true,
        val profile: SharedProfileResponse? = null
    )

    interface Navigator

    interface Factory {

        fun create(c: ComponentContext, n: Navigator, userId: String): OtherProfileComponent
    }

    val state: StateFlow<State>

}

class DefaultOtherProfileComponent(
    private val componentContext: ComponentContext,
    private val navigator: OtherProfileComponent.Navigator,
    private val userId: String,
    private val profileRepo: ProfileRepo,
): OtherProfileComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _state = MutableStateFlow(OtherProfileComponent.State())
    override val state = _state.asStateFlow()

    init {
        scope.launch {
            profileRepo.getSharedProfile(userId).let {
                _state.emit(
                    OtherProfileComponent.State(
                        isLoading = false,
                        profile = it.getOrNull()
                    )
                )
            }
        }
    }
}