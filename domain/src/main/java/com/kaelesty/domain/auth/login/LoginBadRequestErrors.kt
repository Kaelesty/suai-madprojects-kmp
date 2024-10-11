package com.kaelesty.domain.auth.login

enum class LoginBadRequestErrors(val code: Int) {
	USER_NOT_FOUND(1),
	WRONG_PASSWORD(2),
}