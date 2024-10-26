package com.kaelesty.madprojects_kmp.blocs.memberProfile

import kotlinx.coroutines.flow.StateFlow

interface MemberProfileComponent {

    val state: StateFlow<MemberProfileStore.State>

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