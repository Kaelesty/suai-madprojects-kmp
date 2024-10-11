package com.kaelesty.domain.common

import com.kaelesty.domain.auth.login.LoginBadRequestErrors
import com.kaelesty.domain.auth.login.LoginBody

fun main(ucr: UseCaseResult<LoginBody, LoginBadRequestErrors>) {

	when (ucr) {
		is UseCaseResult.Success -> {
			(ucr.body)
		}
		is UseCaseResult.BadRequest -> {
			(ucr.error)
		}
		is UseCaseResult.ExternalError -> {

		}
	}
}