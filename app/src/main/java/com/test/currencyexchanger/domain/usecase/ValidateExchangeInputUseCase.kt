package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.UseCase
import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.domain.validation.Validator
import kotlin.coroutines.CoroutineContext

class ValidateExchangeInputUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val validator: Validator<ExchangeInput>
) : UseCase<ValidateExchangeInputUseCase.Param, Unit>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler,
) {

    override suspend fun executeActual(param: Param) {
        validator.validateOrThrow(value = param.input)
    }

    data class Param(val input: ExchangeInput)
}