package com.kaelesty.madprojects_kmp.blocs.memberProfile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.memberProfile.newProject.NewProjectComponent
import com.kaelesty.madprojects_kmp.blocs.memberProfile.profile.ProfileComponent


interface MemberProfileComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Profile(val component: ProfileComponent): Child
        class NewProject(val component: NewProjectComponent): Child
    }
}