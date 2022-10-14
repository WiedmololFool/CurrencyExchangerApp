package com.test.currencyexchanger.core.usecase

import com.test.currencyexchanger.core.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class FlowUseCase<Param, Return>(
    private val coroutineContext: CoroutineContext,
    private val errorHandler: ErrorHandler,
) {
    protected abstract fun executeActual(param: Param): Flow<Return>

    fun execute(param: Param): Flow<Return> =
        executeActual(param)
            .flowOn(coroutineContext)
            .mapException(errorHandler::process)
}