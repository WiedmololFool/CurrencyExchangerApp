package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.UseCase
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.data.remote.CurrencyDataSource
import kotlin.coroutines.CoroutineContext

class GetAllCurrenciesUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val repository: CurrencyDataSource,
) : UseCase<GetAllCurrenciesUseCase.Param, List<Currency>>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler,
) {

    override suspend fun executeActual(param: Param): List<Currency> =
        repository.getAllCurrencies()

    data class Param(val param: String = "")
}