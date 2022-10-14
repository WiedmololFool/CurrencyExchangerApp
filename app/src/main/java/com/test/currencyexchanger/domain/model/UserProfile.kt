package com.test.currencyexchanger.domain.model

data class UserProfile(
    val balances: List<Balance>,
    val currencyExchangesNumber: Int,
    val commissionFee: Double
)