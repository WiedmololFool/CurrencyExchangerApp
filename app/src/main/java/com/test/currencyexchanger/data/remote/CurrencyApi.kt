package com.test.currencyexchanger.data.remote

import com.test.currencyexchanger.data.remote.model.CurrenciesResponse
import retrofit2.http.GET

interface CurrencyApi {

    @GET(EndPoints.SYMBOLS_URL)
    suspend fun getAllCurrencies(): CurrenciesResponse
}