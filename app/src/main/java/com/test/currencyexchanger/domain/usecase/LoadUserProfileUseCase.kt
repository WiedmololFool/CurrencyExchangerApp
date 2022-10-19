package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.UseCase
import com.test.currencyexchanger.data.repository.UserBalanceRepository
import kotlin.coroutines.CoroutineContext

class LoadUserProfileUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val repository: UserBalanceRepository,
) : UseCase<LoadUserProfileUseCase.Param, Boolean>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler,
) {

    override suspend fun executeActual(param: Param): Boolean {
        return repository.loadUserProfile()
    }

    data class Param(val param: String = "")
}