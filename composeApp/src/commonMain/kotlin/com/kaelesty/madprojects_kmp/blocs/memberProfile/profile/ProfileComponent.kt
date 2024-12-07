package com.kaelesty.madprojects_kmp.blocs.memberProfile.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val state: StateFlow<ProfileStore.State>

    fun onOpenProject(projectId: Int)

    fun onConnectProject()

    fun onCreateProject()

    fun onEditProfile()

    interface Navigator {

        fun editProfile()

        fun openProject(projectId: Int)

        fun connectProject()

        fun createProject()
    }
}