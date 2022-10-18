package com.test.currencyexchanger.domain.model

data class UserProfile(
    val balances: List<Balance>,
    var currencyExchangesNumber: Int,
    val commissionFee: Double
)