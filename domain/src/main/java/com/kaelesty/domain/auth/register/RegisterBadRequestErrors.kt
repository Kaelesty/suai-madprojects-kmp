package com.kaelesty.domain.auth.register

enum class RegisterBadRequestErrors(val code: Int) {
	BAD_EMAIL(1),
	BAD_PASSWORD(2),
}