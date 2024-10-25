package com.kaelesty.madprojects_kmp.blocs.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kaelesty.madprojects_kmp.blocs.auth.AuthComponent
import com.kaelesty.madprojects_kmp.blocs.memberProfile.MemberProfileComponent
import com.kaelesty.madprojects_kmp.blocs.project.ProjectComponent

interface RootComponent {

	val stack: Value<ChildStack<*, Child>>

	sealed interface Child {

		class Auth(val component: AuthComponent): Child

		class Project(val component: ProjectComponent): Child

		class MemberProfile(val component: MemberProfileComponent): Child
	}
}