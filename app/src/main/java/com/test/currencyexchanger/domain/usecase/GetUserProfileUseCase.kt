package com.test.currencyexchanger.domain.usecase

import com.test.currencyexchanger.core.ErrorHandler
import com.test.currencyexchanger.core.usecase.FlowUseCase
import com.test.currencyexchanger.data.repository.UserBalanceRepository
import com.test.currencyexchanger.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class GetUserProfileUseCase(
    coroutineContext: CoroutineContext,
    errorHandler: ErrorHandler,
    private val repository: UserBalanceRepository,
) : FlowUseCase<GetUserProfileUseCase.Param, UserProfile>(
    coroutineContext = coroutineContext,
    errorHandler = errorHandler
) {

    override fun executeActual(param: Param): StateFlow<UserProfile> {
       return repository.getUserProfile()
    }

    data class Param(val param: String = "")
}