package com.test.currencyexchanger.di


import com.test.currencyexchanger.presentation.currency.CurrencyViewModel
import com.test.currencyexchanger.presentation.main.MainViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {

    viewModel {
        MainViewModel(
            loadUserProfileUseCase = get(),
            subscribeOnUserProfileUseCase = get(),
            convertCurrencyUseCase = get(),
            convertBalanceUseCase = get(),
            coroutineContext = get(),
            validateExchangeInputUseCase = get()
        )
    }

    viewModel{
        CurrencyViewModel(
            getAllCurrenciesUseCase = get(),
            coroutineContext = get()
        )
    }
}