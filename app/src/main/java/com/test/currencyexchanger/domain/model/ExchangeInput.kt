package com.test.currencyexchanger.domain.model

data class ExchangeInput(
    val amount: Double = 0.0,
    val soughtCurrency: Currency? = null,
    val boughtCurrency: Currency? = null,
)