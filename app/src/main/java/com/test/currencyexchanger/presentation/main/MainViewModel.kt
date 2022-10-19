package com.test.currencyexchanger.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.domain.usecase.*
import com.test.currencyexchanger.utils.Utility.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val loadUserProfileUseCase: LoadUserProfileUseCase,
    private val subscribeOnUserProfileUseCase: SubscribeOnUserProfileUseCase,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val convertBalanceUseCase: ConvertBalanceUseCase,
    private val validateExchangeInputUseCase: ValidateExchangeInputUseCase,
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
        viewModelScope.launch(coroutineContext) {
            while(true){
                with (viewState.input){
                    if(amount != null && soughtCurrency !=null && boughtCurrency != null){
                        delay(15000L) //  15 seconds because of small amounts of queries in API subscribe plan
                        convertCurrency()
                    }
                }
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch(coroutineContext) {
            loadUserProfileUseCase.execute(LoadUserProfileUseCase.Param()) {
                onStart = {
                    viewState = viewState.copy(showScreenLoadingProgress = true)
                    viewState = viewState.copy(userProfileIsLoaded = false)
                }
                onSuccess = { isLoaded ->
                    viewState = viewState.copy(userProfileIsLoaded = isLoaded)
                }
                onError = { error ->
                    viewState = viewState.copy(error = error)
                }
                onTerminate = {
                    viewState = viewState.copy(showScreenLoadingProgress = false)
                }
            }
        }
    }

    private fun subscribeOnProfile() {
        viewModelScope.launch {
            subscribeOnUserProfileUseCase.execute(SubscribeOnUserProfileUseCase.Param())
                .collect { userProfile ->
                    log("COLLECTED USER PROFILE ${userProfile.commissionFee} ${userProfile.currencyExchangesNumber}")
                    viewState = viewState.copy(userProfile = userProfile)
                    //todo add flag to viewState which will display that userProfile succesfully loaded instead of list check
                    if (userProfile.balances.isNotEmpty()) {
                        convertCurrency()
                    }
                }
        }
    }

    private fun convertCurrency() {
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
                        viewState = viewState.copy(showConversionProgress = true)
                    }
                    onSuccess = { convertedBalance ->
                        viewState = viewState.copy(convertedInputValue = convertedBalance.value)
                    }
                    onError = { error ->
                        viewState = viewState.copy(error = error)
                    }
                    onTerminate = {
                        viewState = viewState.copy(showConversionProgress = false)
                    }
                }
            }
        }
    }

   private fun internalConvertBalance() {
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
                    viewState = viewState.copy(showScreenLoadingProgress = true)
                }
                onSuccess = { isConverted ->
                    log("Currency successfully converted $isConverted")
                    showCurrencyConvertedDialog(isConverted)
                }
                onError = { error ->
                    viewState = viewState.copy(error = error)
                }
                onTerminate = {
                    viewState = viewState.copy(showScreenLoadingProgress = false)
                }
            }
        }
    }

    fun convertBalance() {
        viewModelScope.launch {
            validateExchangeInputUseCase.execute(param = ValidateExchangeInputUseCase.Param(input = viewState.input)) {
                onSuccess = {
                   internalConvertBalance()
                }
                onError = { error ->
                    viewState = viewState.copy(error = error)
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