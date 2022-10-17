package com.test.currencyexchanger.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.usecase.ConvertCurrencyUseCase
import com.test.currencyexchanger.domain.usecase.GetUserProfileUseCase
import com.test.currencyexchanger.domain.usecase.LoadUserProfileUseCase
import com.test.currencyexchanger.utils.Utility.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val loadUserProfileUseCase: LoadUserProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    var viewState by mutableStateOf(value = MainViewState())
        private set

    fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            loadUserProfileUseCase.execute(LoadUserProfileUseCase.Param()) {
                onStart = {
                    viewState = viewState.copy(showProgress = true)
                }
                onSuccess = { isLoaded ->
                    getUserProfile()
                }
                onError = { error ->
                    viewState = viewState.copy(error = error)
                }
                onTerminate = {
                    viewState = viewState.copy(showProgress = false)
                }
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase.execute(GetUserProfileUseCase.Param())
                .collect { userProfile ->
                    viewState = viewState.copy(userProfile = userProfile)
                    log(
                        "userProfile: ${
                            userProfile.balances.first { it.currency.symbol == "EUR" }
                        } ${userProfile.commissionFee} ${userProfile.currencyExchangesNumber}"
                    )
                    val soughtBalance =
                        userProfile.balances.first { it.currency.symbol == "EUR" }
                            .copy(value = 100.0)
                    val boughtBalance =
                        userProfile.balances.first { it.currency.symbol == "USD" }
                    mockUserBalances(
                        soughtBalance = soughtBalance,
                        boughtBalance = boughtBalance
                    )
                    convertCurrency()
                }
        }
    }

    fun convertCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            with(viewState) {
                convertCurrencyUseCase.execute(
                    ConvertCurrencyUseCase.Param(
                        amount = soughtBalance.value,
                        from = soughtBalance.currency,
                        to = boughtBalance.currency
                    )
                ) {
                    onStart = {
                        viewState = viewState.copy(showProgress = true)
                    }
                    onSuccess = { convertedBalance ->
                       viewState = viewState.copy(boughtBalance = convertedBalance)
                    }
                    onError = { error ->
                        viewState = viewState.copy(error = error)
                    }
                    onTerminate = {
                        viewState = viewState.copy(showProgress = false)
                    }
                }
            }
        }
    }

    private fun mockUserBalances(soughtBalance: Balance, boughtBalance: Balance) {
        viewState = viewState.copy(soughtBalance = soughtBalance, boughtBalance = boughtBalance)
    }
}