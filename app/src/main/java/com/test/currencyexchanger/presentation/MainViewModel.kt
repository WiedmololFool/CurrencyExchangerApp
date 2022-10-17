package com.test.currencyexchanger.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.domain.model.ExchangeInput
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

    fun updateInput(input: ExchangeInput) {
        viewModelScope.launch {
            viewState = viewState.copy(input = input)
            convertCurrency()
        }
    }

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
                    val soughtCurrency =
                        userProfile.balances.first { it.currency.symbol == "EUR" }.currency
                    val boughtCurrency =
                        userProfile.balances.first { it.currency.symbol == "USD" }.currency
                    createExchangeInput(
                        amount = 100.0,
                        soughtCurrency = soughtCurrency,
                        boughtCurrency = boughtCurrency
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
                        amount = input.amount ?: 0.0,
                        from = input.soughtCurrency ?: Currency("", ""),
                        to = input.boughtCurrency ?: Currency("", "")
                    )
                ) {
                    onStart = {
//                        viewState = viewState.copy(showProgress = true)
                    }
                    onSuccess = { convertedBalance ->
                        viewState = viewState.copy(convertedInputValue = convertedBalance.value)
                    }
                    onError = { error ->
                        viewState = viewState.copy(error = error)
                    }
                    onTerminate = {
//                        viewState = viewState.copy(showProgress = false)
                    }
                }
            }
        }
    }

    private fun createExchangeInput(
        amount: Double,
        soughtCurrency: Currency,
        boughtCurrency: Currency
    ) {
        viewState = viewState.copy(
            input = ExchangeInput(
                soughtCurrency = soughtCurrency,
                boughtCurrency = boughtCurrency
            )
        )
    }
}