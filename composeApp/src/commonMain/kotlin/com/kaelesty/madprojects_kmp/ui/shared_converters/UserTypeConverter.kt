package com.kaelesty.madprojects_kmp.ui.shared_converters

import com.kaelesty.domain.common.UserType

fun UserType.toUi() = when(this) {
	UserType.STUDENT -> "Студент"
	UserType.CURATOR -> "Преподаватель"
}