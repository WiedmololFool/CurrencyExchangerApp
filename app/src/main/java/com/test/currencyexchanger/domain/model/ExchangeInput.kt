package com.test.currencyexchanger.domain.model

data class ExchangeInput(
    val amount: String? = null,
    val soughtCurrency: Currency? = null,
    val boughtCurrency: Currency? = null,
)