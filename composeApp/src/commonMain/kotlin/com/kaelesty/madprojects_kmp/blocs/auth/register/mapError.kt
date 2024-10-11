package com.kaelesty.madprojects_kmp.blocs.auth.register

import com.kaelesty.domain.auth.register.RegisterBadRequestErrors

fun RegisterBadRequestErrors.ui()= when (this) {
	RegisterBadRequestErrors.BAD_GITHUB -> "Не удалось загрузить профиль Github"
	RegisterBadRequestErrors.USED_GITHUD -> "Пользователь с таким Github уже существует"
	RegisterBadRequestErrors.USED_EMAIL -> "Пользователь с таким email уже существует"
}