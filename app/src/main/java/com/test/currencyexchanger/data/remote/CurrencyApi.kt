package com.test.currencyexchanger.data.remote

import com.test.currencyexchanger.data.remote.model.ConvertedCurrencyResponse
import com.test.currencyexchanger.data.remote.model.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET(EndPoints.SYMBOLS_URL)
    suspend fun getAllCurrencies(): CurrenciesResponse

    @GET(EndPoints.CONVERT_URL)
    suspend fun convertCurrency(
        @Query("amount") amount: String,
        @Query("from") from: String,
        @Query("to") to: String,
    ): ConvertedCurrencyResponse
}