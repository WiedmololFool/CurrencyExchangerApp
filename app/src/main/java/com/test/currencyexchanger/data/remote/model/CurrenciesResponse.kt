package com.test.currencyexchanger.data.remote.model

import com.google.gson.annotations.SerializedName
import com.test.currencyexchanger.domain.model.Currency

data class CurrenciesResponse(
    val success: Boolean,
    @SerializedName("symbols")
    val results: HashMap<String, String>
)