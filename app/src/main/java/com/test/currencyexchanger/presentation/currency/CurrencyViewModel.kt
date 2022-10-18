package com.test.currencyexchanger.presentation.currency


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyexchanger.domain.usecase.GetAllCurrenciesUseCase
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CurrencyViewModel(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val coroutineContext: CoroutineContext,
) : ViewModel() {

    var viewState by mutableStateOf(CurrencyViewState())
        private set

    fun loadCurrencies() {
        viewModelScope.launch(coroutineContext) {
            getAllCurrenciesUseCase.execute(param = GetAllCurrenciesUseCase.Param()) {
                onStart = {
                    viewState = viewState.copy(showProgress = true)
                }
                onSuccess = { currencies ->
                    viewState = viewState.copy(showProgress = false, currencies = currencies)
                }
                onError = { error ->
                    viewState = viewState.copy(error = error)
                }
                onTerminate = { _ ->
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
}