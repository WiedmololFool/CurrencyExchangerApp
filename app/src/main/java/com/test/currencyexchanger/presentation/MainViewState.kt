package com.test.currencyexchanger.presentation

import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.domain.model.UserProfile

data class MainViewState(
    val userProfile: UserProfile? = null,
    val showProgress: Boolean = false,
    val error: Throwable? = null,
    val soughtBalance: Balance = Balance(
        currency = Currency(symbol = "", name = ""),
        value = 0.0
    ),
    val boughtBalance: Balance = Balance(
        currency = Currency(symbol = "", name = ""),
        value = 0.0
    )
)