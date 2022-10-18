package com.test.currencyexchanger.data.remote

import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.utils.Utility.isNumeric

class CurrencyDataSourceImpl(private val currencyApi: CurrencyApi) : CurrencyDataSource {

    override suspend fun getAllCurrencies(): List<Currency> {
        val response = currencyApi.getAllCurrencies().results.toList().map {
            Currency(it.first, it.second)
        }.toMutableList().apply {
            find { it.symbol == "EUR" }.let { eur ->
                remove(eur)
                if (eur != null) {
                    this.add(0, eur)
                }
            }
            find { it.symbol == "USD" }.let { usd ->
                remove(usd)
                if (usd != null) {
                    this.add(1, usd)
                }
            }
        }
        return response.toList()
    }

    override suspend fun convertCurrency(
        amount: String,
        from: Currency,
        to: Currency
    ): Balance {
        return if (amount.isNotEmpty() && amount.isNumeric() && amount.toDouble() != 0.0) {
            val response = currencyApi.convertCurrency(
                amount = amount,
                from = from.symbol,
                to = to.symbol
            )
            Balance(currency = to, response.result.toDouble())
        } else {
            Balance(currency = to, 0.0)
        }

    }

}