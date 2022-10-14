package com.test.currencyexchanger.core.usecase

import com.test.currencyexchanger.core.ErrorHandler
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class UseCase<Param, Result>(
    private val coroutineContext: CoroutineContext,
    private val errorHandler: ErrorHandler,
) {

    protected abstract suspend fun executeActual(param: Param): Result

    @Suppress("TooGenericExceptionCaught")
    suspend fun execute(
        param: Param,
        result: Callback<Result> = {},
    ) {
        val resultReceiver = ResultCallback<Result>()
            .apply(result)
            .also {
                it.onStart?.invoke()
            }

        runCatching {
            withContext(coroutineContext) { executeActual(param) }
        }.onSuccess { success ->
            resultReceiver.onSuccess?.invoke(success)
        }.mapException { exception ->
            errorHandler.process(exception)
        }.onFailed { mappedException ->
            resultReceiver.onError?.invoke(mappedException)
        }.finally { isCanceled ->
            resultReceiver.onTerminate?.invoke(isCanceled)
        }
    }
}