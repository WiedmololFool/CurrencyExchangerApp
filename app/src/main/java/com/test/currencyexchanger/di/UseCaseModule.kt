package com.test.currencyexchanger.di

import com.test.currencyexchanger.domain.usecase.GetAllCurrenciesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetAllCurrenciesUseCase(
            coroutineContext = get(),
            errorHandler = get(),
            repository = get()
        )
    }
}