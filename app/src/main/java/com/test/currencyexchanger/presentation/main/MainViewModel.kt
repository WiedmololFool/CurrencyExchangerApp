package com.test.currencyexchanger.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.domain.usecase.ConvertBalanceUseCase
import com.test.currencyexchanger.domain.usecase.ConvertCurrencyUseCase
import com.test.currencyexchanger.domain.usecase.SubscribeOnUserProfileUseCase
import com.test.currencyexchanger.domain.usecase.LoadUserProfileUseCase
import com.test.currencyexchanger.utils.Utility.log
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val loadUserProfileUseCase: LoadUserProfileUseCase,
    private val subscribeOnUserProfileUseCase: SubscribeOnUserProfileUseCase,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val convertBalanceUseCase: ConvertBalanceUseCase,
    private val coroutineContext: CoroutineContext,
) : ViewModel() {

    var viewState by mutableStateOf(value = MainViewState())
        private set

    fun updateInput(input: ExchangeInput) {
        viewModelScope.launch {
            viewState = viewState.copy(input = input)
            convertCurrency()
        }
    }

    init {
        subscribeOnProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch(coroutineContext) {
            loadUserProfileUseCase.execute(LoadUserProfileUseCase.Param()) {
                onStart = {
                    viewState = viewState.copy(showProgress = true)
                    viewState = viewState.copy(userProfileIsLoaded = false)
                }
                onSuccess = { isLoaded ->
                    viewState = viewState.copy(userProfileIsLoaded = isLoaded)
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

    fun subscribeOnProfile() {
        viewModelScope.launch {
            subscribeOnUserProfileUseCase.execute(SubscribeOnUserProfileUseCase.Param())
                .collect { userProfile ->
                    log("COLLECTED USER PROFILE ${userProfile.commissionFee} ${userProfile.currencyExchangesNumber}")
                    viewState = viewState.copy(userProfile = userProfile)
//                    log(
//                        "userProfile: ${
//                            userProfile.balances.first { it.currency.symbol == "EUR" }
//                        } ${userProfile.commissionFee} ${userProfile.currencyExchangesNumber}"
//                    )
                    //todo add flag to viewState which will display that userProfile succesfully loaded instead of list check
                    if (userProfile.balances.isNotEmpty()) {
////                    if (viewState.userProfileIsLoaded) {
//                        val soughtCurrency =
//                            userProfile.balances.first { it.currency.symbol == "EUR" }.currency
//                        val boughtCurrency =
//                            userProfile.balances.first { it.currency.symbol == "USD" }.currency
//                        createExchangeInput(
//                            amount = 100.0,
//                            soughtCurrency = soughtCurrency,
//                            boughtCurrency = boughtCurrency
//                        )
                        convertCurrency()
                    }
                }
        }
    }

    fun convertCurrency() {
        viewModelScope.launch(coroutineContext) {
            with(viewState) {
                convertCurrencyUseCase.execute(
                    ConvertCurrencyUseCase.Param(
                        amount = input.amount ?: "0.0",
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

    fun convertBalance() {
        viewModelScope.launch {
            convertBalanceUseCase.execute(
                ConvertBalanceUseCase.Param(
                    soughtBalance = Balance(
                        currency = requireNotNull(viewState.input.soughtCurrency),
                        value = requireNotNull(viewState.input.amount).toDouble()
                    ),
                    boughtBalance = Balance(
                        currency = requireNotNull(viewState.input.boughtCurrency),
                        value = requireNotNull(viewState.convertedInputValue)
                    )
                )
            ) {
                onStart = {
                    viewState = viewState.copy(showProgress = true)
                }
                onSuccess = { isConverted ->
                    log("Currency successfully converted $isConverted")
                    showCurrencyConvertedDialog(isConverted)
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

    fun onErrorShown() {
        viewModelScope.launch {
            viewState = viewState.copy(error = null)
        }
    }

    fun showCurrencyConvertedDialog(show: Boolean) {
        viewState = viewState.copy(showCurrencyConvertedDialog = show)
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