package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.UseCase
import com.test.currencyexchanger.data.repository.UserBalanceRepository
import com.test.currencyexchanger.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class LoadUserProfileUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val repository: UserBalanceRepository,
) : UseCase<LoadUserProfileUseCase.Param, StateFlow<UserProfile>>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler,
) {

    override suspend fun executeActual(param: Param): StateFlow<UserProfile> {
        return repository.loadUserProfile()
    }

    data class Param(val param: String = "")
}