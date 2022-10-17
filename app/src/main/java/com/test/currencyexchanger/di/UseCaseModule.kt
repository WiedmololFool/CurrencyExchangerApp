package com.test.currencyexchanger.di

import com.test.currencyexchanger.domain.usecase.ConvertCurrencyUseCase
import com.test.currencyexchanger.domain.usecase.GetAllCurrenciesUseCase
import com.test.currencyexchanger.domain.usecase.GetUserProfileUseCase
import com.test.currencyexchanger.domain.usecase.LoadUserProfileUseCase
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
        GetUserProfileUseCase(
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
}