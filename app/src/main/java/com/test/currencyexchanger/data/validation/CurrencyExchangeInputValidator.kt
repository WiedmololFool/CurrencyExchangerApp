package com.test.currencyexchanger.data.validation

import com.test.currencyexchanger.domain.exception.ExchangeInputValidationException
import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.domain.validation.Validator

class CurrencyExchangeInputValidator : Validator<ExchangeInput> {

    @Throws(Exception::class)
    override suspend fun validateOrThrow(value: ExchangeInput) {
        validateNotEmpty(value = value)
    }

    @Throws(ExchangeInputValidationException::class)
    private fun validateNotEmpty(value: ExchangeInput) {
        when {
            value.soughtCurrency == null ->
                ExchangeInputValidationException.EmptySellCurrencyError
            value.boughtCurrency == null ->
                ExchangeInputValidationException.EmptyReceiveCurrencyError
            value.amount.isNullOrBlank() ->
                ExchangeInputValidationException.EmptySellAmountError
            value.soughtCurrency == value.boughtCurrency ->
                ExchangeInputValidationException.SameCurrencyError
            else -> {
                null
            }
        }?.run { throw this }
    }




}