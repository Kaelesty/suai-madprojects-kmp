package com.kaelesty.madprojects.shared

fun validateEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return regex.matches(email)
}

fun validatePassword(password: String) = password.length >= 8