package com.test.currencyexchanger.presentation.main

import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.domain.model.UserProfile

data class MainViewState(
    val userProfile: UserProfile? = null,
    val showScreenLoadingProgress: Boolean = false,
    val userProfileIsLoaded: Boolean = false,
    val error: Throwable? = null,
    val showConversionProgress: Boolean = false,
    val showCurrencyConvertedDialog: Boolean = false,
    val input: ExchangeInput = ExchangeInput(),
    val convertedInputValue: Double = 0.0
)