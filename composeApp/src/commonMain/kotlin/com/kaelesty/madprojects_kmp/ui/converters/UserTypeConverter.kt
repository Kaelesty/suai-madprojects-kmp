package com.kaelesty.madprojects_kmp.ui.converters

import com.kaelesty.domain.common.UserType

fun UserType.toUi() = when(this) {
	UserType.STUDENT -> "Студент"
	UserType.CURATOR -> "Преподаватель"
}