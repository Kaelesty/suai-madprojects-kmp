package com.kaelesty.domain.common

sealed interface UseCaseResult<B, E> {

	class Success<B, E>(val body: B): UseCaseResult<B, E>

	class BadRequest<B, E>(val error: E): UseCaseResult<B, E>

	class ExternalError<B, E>: UseCaseResult<B, E>
}

