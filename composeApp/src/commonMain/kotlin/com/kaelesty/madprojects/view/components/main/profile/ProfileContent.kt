package com.kaelesty.madprojects.view.components.main.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kaelesty.madprojects.domain.stores.ProfileStore
import com.kaelesty.madprojects.view.components.shared.ErrorScreen
import com.kaelesty.madprojects.view.components.shared.LoadingScreen

@Composable
fun CommonProfileContent(
    component: ProfileComponent
) {

    val state by component.state.collectAsState()

    when (val instance = state) {
        is ProfileStore.State.CommonProfile -> CommonProfileScreen(
            instance,
            onCreateNewProject = { component.toProjectCreation() },
            onProjectClick = {},
        )
        is ProfileStore.State.CuratorProfile -> CuratorProfileScreen(instance)
        ProfileStore.State.Error -> ErrorScreen()
        ProfileStore.State.Loading -> LoadingScreen()
    }
}