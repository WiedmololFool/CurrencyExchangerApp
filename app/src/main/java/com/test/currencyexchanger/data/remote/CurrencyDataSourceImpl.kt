package com.test.currencyexchanger.data.remote

import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency

class CurrencyDataSourceImpl(private val currencyApi: CurrencyApi) : CurrencyDataSource {

    override suspend fun getAllCurrencies(): List<Currency> {
        val response = currencyApi.getAllCurrencies().results.toList().map {
            Currency(it.first, it.second)
        }
        return response
    }

    override suspend fun convertCurrency(
        amount: Double,
        from: Currency,
        to: Currency
    ): Balance {
        val response = currencyApi.convertCurrency(
            amount = amount.toString(),
            from = from.symbol,
            to = to.symbol
        )
        return Balance(currency = to, response.result.toDouble())
    }

}