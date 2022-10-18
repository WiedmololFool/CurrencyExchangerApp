package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.UseCase
import com.test.currencyexchanger.data.remote.CurrencyDataSource
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import kotlin.coroutines.CoroutineContext

class ConvertCurrencyUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val repository: CurrencyDataSource,
) : UseCase<ConvertCurrencyUseCase.Param, Balance>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler,
) {

    override suspend fun executeActual(param: Param): Balance {
        return repository.convertCurrency(
            amount = param.amount,
            from = param.from,
            to = param.to
        )
    }

    data class Param(
        val amount: String,
        val from: Currency,
        val to: Currency
    )
}