package com.kaelesty.madprojects.domain

enum class UserType {
    Common, Curator
}

fun UserType.toUi() = when(this) {
    UserType.Common -> "Студент"
    UserType.Curator -> "Преподаватель"
}