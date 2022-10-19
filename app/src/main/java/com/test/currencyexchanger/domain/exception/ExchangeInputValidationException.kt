package com.test.currencyexchanger.domain.exception

sealed class ExchangeInputValidationException(message: String) : Throwable(message = message) {
    object EmptySellAmountError : ExchangeInputValidationException("Sell amount is empty")
    object SameCurrencyError : ExchangeInputValidationException("Sell currency cannot be equal to bought")
    object EmptySellCurrencyError : ExchangeInputValidationException("Sell currency is empty")
    object EmptyReceiveCurrencyError : ExchangeInputValidationException("Receive currency is empty")
}