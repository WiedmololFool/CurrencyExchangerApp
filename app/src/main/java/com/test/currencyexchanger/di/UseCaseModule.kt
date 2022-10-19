package com.test.currencyexchanger.di

import com.test.currencyexchanger.data.validation.CurrencyExchangeInputValidator
import com.test.currencyexchanger.domain.usecase.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetAllCurrenciesUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            repository = get()
        )
    }

    single {
        LoadUserProfileUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            repository = get()
        )
    }

    single {
        SubscribeOnUserProfileUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            repository = get()
        )
    }

    single {
        ConvertCurrencyUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            repository = get()
        )
    }

    single {
        ConvertBalanceUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            repository = get()
        )
    }

    single {
        ValidateExchangeInputUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            validator = get(named<CurrencyExchangeInputValidator>())
        )
    }
}