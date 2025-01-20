package com.kaelesty.madprojects.domain

import kotlinx.serialization.Serializable

@Serializable
enum class UserType {
    Common, Curator
}

fun UserType.toUi() = when(this) {
    UserType.Common -> "Студент"
    UserType.Curator -> "Преподаватель"
}