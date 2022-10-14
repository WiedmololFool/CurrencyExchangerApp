package com.test.currencyexchanger.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.UserProfile

class SharedPrefUserProfileStorage(context: Context) : UserProfileStorage {

    private val sharedPreferences = context.getSharedPreferences(
        SharedPrefConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE
    )

    override fun save(userProfile: UserProfile): Boolean {
        with(SharedPrefConstants) {
            val convertedUserBalances = Gson().toJson(userProfile.balances)
            sharedPreferences.edit().putString(KEY_USER_BALANCES, convertedUserBalances).apply()
            sharedPreferences.edit()
                .putInt(
                    KEY_USER_CURRENCY_EXCHANGES_NUMBER,
                    userProfile.currencyExchangesNumber
                )
                .apply()
            sharedPreferences.edit()
                .putFloat(
                    KEY_USER_CURRENCY_EXCHANGES_NUMBER,
                    userProfile.currencyExchangesNumber.toFloat()
                )
                .apply()
        }
        return true
    }

    override fun get(): UserProfile {
        with(SharedPrefConstants) {
            val retrievedUserBalancesString = sharedPreferences.getString(KEY_USER_BALANCES, null)
            val userBalances =
                retrievedUserBalancesString?.let { Gson().fromJson<List<Balance>>(it) }
                    ?: emptyList()
            val currencyExchangesNumber = sharedPreferences.getInt(
                KEY_USER_CURRENCY_EXCHANGES_NUMBER, DEFAULT_CURRENCY_EXCHANGES_NUMBER
            )
            val commissionFee =
                sharedPreferences.getFloat(KEY_COMMISSION_FEE, DEFAULT_COMMISSION_FEE.toFloat())
            return UserProfile(
                balances = userBalances,
                currencyExchangesNumber = currencyExchangesNumber,
                commissionFee = commissionFee.toDouble()
            )
        }
    }

    override fun clear(): Boolean {
        sharedPreferences.edit().clear().apply()
        return true
    }

    inline fun <reified T> Gson.fromJson(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

}