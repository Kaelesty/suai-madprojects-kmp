package com.kaelesty.domain.auth.register

enum class RegisterBadRequestErrors(val code: Int) {
	//BAD_EMAIL(1),
	//BAD_PASSWORD(2),
	BAD_GITHUB(3),
	USED_GITHUD(4),
	USED_EMAIL(5),
}