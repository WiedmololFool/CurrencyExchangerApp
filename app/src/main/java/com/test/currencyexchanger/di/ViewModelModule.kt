package com.test.currencyexchanger.di


import com.test.currencyexchanger.presentation.MainViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {

   viewModel {
      MainViewModel(userBalanceRepository = get())
   }
}