package com.test.currencyexchanger

import android.app.Application
import com.test.currencyexchanger.di.appModule
import com.test.currencyexchanger.di.dataModule
import com.test.currencyexchanger.di.useCaseModule
import com.test.currencyexchanger.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AppCurrencyExchanger: Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AppCurrencyExchanger)
            modules(listOf(appModule,dataModule, viewModelModule, useCaseModule))
        }
    }
}