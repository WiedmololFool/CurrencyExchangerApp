package com.test.currencyexchanger.data.remote

import com.test.currencyexchanger.domain.model.Currency

interface CurrencyDataSource {

    suspend fun getAllCurrencies(): List<Currency>
}