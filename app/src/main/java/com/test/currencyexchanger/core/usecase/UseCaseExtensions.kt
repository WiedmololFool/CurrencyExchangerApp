package com.test.currencyexchanger.core.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlin.coroutines.coroutineContext

suspend fun <Param, Return> UseCase<Param, Return>.executeOrNull(param: Param): Return? {
    var result: Return? = null
    val proxy: Callback<Return> = {
        onSuccess = { result = it }
    }
    execute(param, proxy)
    coroutineContext.ensureActive()
    return result
}

suspend fun <Param, Return> UseCase<Param, Return>.executeOrThrow(param: Param): Return {
    var result: Return? = null
    val proxy: Callback<Return> = {
        onSuccess = { result = it }
        onError = { throw it }
    }
    execute(param, proxy)
    coroutineContext.ensureActive()
    return checkNotNull(result) { "Success result wasn't received for $this" }
}

internal fun <T> Result<T>.onFailed(
    block: (exception: Throwable) -> Unit
): Result<T> {
    val exception = exceptionOrNull() ?: return this
    if (exception !is CancellationException) {
        block(exception)
    }
    return this
}

internal fun <T> Result<T>.finally(
    block: (isCanceled: Boolean) -> Unit
) {
    block(exceptionOrNull() is CancellationException)
}

@Suppress("TooGenericExceptionCaught")
internal fun <T> Result<T>.mapException(mapper: (Throwable) -> Throwable): Result<T> {
    val exception = exceptionOrNull() ?: return this
    return try {
        Result.failure(mapper(exception))
    } catch (exception: Throwable) {
        Result.failure(exception)
    }
}

internal fun <T> Flow<T>.mapException(mapper: (Throwable) -> Throwable): Flow<T> {
    return catch { error ->
        throw if (error !is CancellationException) {
            mapper(error)
        } else {
            error
        }
    }
}