package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.UseCase
import com.test.currencyexchanger.data.repository.UserBalanceRepository
import com.test.currencyexchanger.domain.model.Balance
import kotlin.coroutines.CoroutineContext

class ConvertBalanceUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val repository: UserBalanceRepository,
) : UseCase<ConvertBalanceUseCase.Param, Boolean>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler,
) {

    override suspend fun executeActual(param: Param): Boolean {
        return repository.convertBalance(
            soughtBalance = param.soughtBalance,
            boughtBalance = param.boughtBalance
        )
    }

    data class Param(val soughtBalance: Balance, val boughtBalance: Balance)
}