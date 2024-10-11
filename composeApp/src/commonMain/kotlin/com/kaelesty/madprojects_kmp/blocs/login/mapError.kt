package com.kaelesty.madprojects_kmp.blocs.login

import com.kaelesty.domain.auth.login.LoginBadRequestErrors

fun LoginBadRequestErrors.ui()= when (this) {
	LoginBadRequestErrors.USER_NOT_FOUND -> "Пользователь с таким email не найден"
	LoginBadRequestErrors.WRONG_PASSWORD -> "Введен неверный пароль"
}