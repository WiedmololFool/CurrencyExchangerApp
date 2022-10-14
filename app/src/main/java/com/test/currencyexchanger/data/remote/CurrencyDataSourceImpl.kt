package com.test.currencyexchanger.data.remote

import com.test.currencyexchanger.domain.model.Currency

class CurrencyDataSourceImpl(private val currencyApi: CurrencyApi) : CurrencyDataSource {

    override suspend fun getAllCurrencies(): List<Currency> {
        val result = currencyApi.getAllCurrencies().results.toList().map {
            Currency(it.first, it.second)
        }
        return result
    }

}