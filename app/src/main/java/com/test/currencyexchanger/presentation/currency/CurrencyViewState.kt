package com.test.currencyexchanger.presentation.currency

import com.test.currencyexchanger.domain.model.Currency

data class CurrencyViewState(
    val showProgress: Boolean = false,
    val currencies: List<Currency> = listOf(),
    val error: Throwable? = null,
)