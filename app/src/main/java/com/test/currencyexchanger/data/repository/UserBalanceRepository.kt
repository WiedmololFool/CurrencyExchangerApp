package com.test.currencyexchanger.data.repository

import com.test.currencyexchanger.data.local.UserProfileStorage
import com.test.currencyexchanger.data.remote.CurrencyDataSource
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserBalanceRepository(
    private val userProfileStorage: UserProfileStorage,
    private val currencyDataSource: CurrencyDataSource
) {

    private val _userBalance = MutableStateFlow(listOf<Balance>())
    val userBalance = _userBalance.asStateFlow()

    private val userProfile = MutableStateFlow(
        UserProfile(
            balances = listOf(),
            currencyExchangesNumber = 0,
            commissionFee = 0.00
        )
    )


    private suspend fun fetchAllCurrencies(): List<Currency> {
        return currencyDataSource.getAllCurrencies()
    }

    private fun createStartBalances(actualCurrencies: List<Currency>): List<Balance> {
        val result = actualCurrencies.map { currency ->
            Balance(
                currency = currency,
                value = if (currency.symbol == "EUR") 1000.00 else 0.00
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
        println("MY_TAG Creating start user profile: $newUserProfile")
        userProfileStorage.save(newUserProfile)
    }


    private fun checkForCurrenciesUpdate(
        localUserProfile: UserProfile,
        actualCurrencies: List<Currency>
    ): UserProfile {
        val localCurrencies = localUserProfile.balances.map { balance ->
            balance.currency
        }
        if (localCurrencies == actualCurrencies) {
            return localUserProfile
        } else {
//            val updatedBalances = localUserProfile.balances.map { balance ->
//                Balance(
//                    currency =
//                )
//            }
            when {
                localCurrencies.size < actualCurrencies.size ->{
                    println("localCurrencies.size < actualCurrencies.size")
                }
                localCurrencies.size > actualCurrencies.size ->{
                    println("localCurrencies.size > actualCurrencies.size")
                }
                else -> {
                    println("localCurrencies.size = actualCurrencies.size")
                }
            }
            //TODO change return result
            return localUserProfile.copy()
        }
    }

    suspend fun loadUserProfile(): StateFlow<UserProfile> {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return userProfile.asStateFlow()
    }


}