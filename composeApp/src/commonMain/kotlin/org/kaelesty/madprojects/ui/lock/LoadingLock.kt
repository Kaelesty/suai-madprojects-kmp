package com.kaelesty.madprojects_kmp.ui.lock

import androidx.compose.runtime.Composable
import com.kaelesty.domain.common.UseCaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingLock: Lock {

	private val _state: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
	override val state
		get() = _state.asStateFlow()

	override suspend fun <B, E> proceed(block: suspend () -> UseCaseResult<B, E>): UseCaseResult<B, E> {
		_state.emit { Loading() }
		return try {
			val res = block()
			_state.emit(null)
			res
		}
		catch (e: Exception) {
			_state.emit { UnexpectedExceptionDialog(
				e,
				onDismiss = {
					_state.update { null }
				}
			) }
			UseCaseResult.ExternalError()
		}
	}
}