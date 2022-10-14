package com.test.currencyexchanger.di

import com.test.currencyexchanger.core.ErrorHandler
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {

    single<CoroutineContext> {
        Dispatchers.IO
    }

    single {
        ErrorHandler { error -> error }
    }

}