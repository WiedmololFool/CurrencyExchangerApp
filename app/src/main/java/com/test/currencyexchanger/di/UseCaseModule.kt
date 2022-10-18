package com.test.currencyexchanger.di

import com.test.currencyexchanger.domain.usecase.*
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
}