package com.test.currencyexchanger.data.repository

import com.test.currencyexchanger.data.local.UserProfileStorage
import com.test.currencyexchanger.data.remote.CurrencyDataSource
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserBalanceRepository(
    private val userProfileStorage: UserProfileStorage,
    private val currencyDataSource: CurrencyDataSource
) {

    private val userProfile = MutableStateFlow(
        UserProfile(
            balances = listOf(),
            currencyExchangesNumber = 0,
            commissionFee = 0.0
        )
    )

    private suspend fun fetchAllCurrencies(): List<Currency> {
        return currencyDataSource.getAllCurrencies()
    }

    private fun createStartBalances(actualCurrencies: List<Currency>): List<Balance> {
        val result = actualCurrencies.map { currency ->
            Balance(
                currency = currency,
                value = if (currency.symbol == "EUR") 1000.0 else 0.0
            )
        }
        return result
    }

    private fun createStartUserProfile(actualCurrencies: List<Currency>) {
        val newUserProfile = UserProfile(
            balances = createStartBalances(actualCurrencies),
            currencyExchangesNumber = 0,
            commissionFee = 0.00
        )
        userProfileStorage.save(newUserProfile)
    }


    private fun checkForCurrenciesUpdate(
        localUserProfile: UserProfile,
        actualCurrencies: List<Currency>
    ): UserProfile {
        val updatedBalances = actualCurrencies.map { actualCurrency ->
            val localBalance = localUserProfile.balances.find {
                it.currency.name == actualCurrency.name
            }
            localBalance ?: Balance(currency = actualCurrency, value = 0.0)
        }
        return localUserProfile.copy(balances = updatedBalances)
    }

    suspend fun loadUserProfile(): Boolean {
        try {
            val localUserProfile = userProfileStorage.get()
            val actualCurrencies = fetchAllCurrencies()
            if (localUserProfile.currencyExchangesNumber == 0) {
                createStartUserProfile(actualCurrencies)
                userProfile.value = userProfileStorage.get()
            } else {
                userProfile.value = checkForCurrenciesUpdate(
                    localUserProfile = localUserProfile,
                    actualCurrencies = actualCurrencies
                )
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            throw(e)
        }
    }

    fun getUserProfile(): StateFlow<UserProfile> {
        return userProfile.asStateFlow()
    }

//    private suspend fun convertCurrency(
//        amount: Double,
//        from: Currency,
//        to: Currency
//    ): Balance {
//        return currencyDataSource.convertCurrency(
//            amount = amount,
//            from = from,
//            to = to
//        )
//    }


}