package com.kaelesty.madprojects_kmp.ui.lock

import androidx.compose.runtime.Composable
import com.kaelesty.domain.common.UseCaseResult
import kotlinx.coroutines.flow.StateFlow

interface Lock {

	val state: StateFlow<(@Composable () -> Unit)?>

	suspend fun <B, E> proceed(
		block: suspend () -> UseCaseResult<B, E>
	): UseCaseResult<B, E>
}