package com.test.currencyexchanger.presentation

import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.domain.model.UserProfile

data class MainViewState(
    val userProfile: UserProfile? = null,
    val showProgress: Boolean = false,
    val error: Throwable? = null,
    val input: ExchangeInput = ExchangeInput(),
    val convertedInputValue: Double = 0.0
)