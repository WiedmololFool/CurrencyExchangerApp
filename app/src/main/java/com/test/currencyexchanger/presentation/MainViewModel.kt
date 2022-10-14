package com.test.currencyexchanger.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.currencyexchanger.data.repository.UserBalanceRepository
import com.test.currencyexchanger.domain.usecase.GetAllCurrenciesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
//    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase
    private val userBalanceRepository: UserBalanceRepository
) : ViewModel() {


//    fun fetchAllCurrencies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            getAllCurrenciesUseCase.execute(GetAllCurrenciesUseCase.Param()){
//                onStart = {
//                    Log.e("MY_TAG", "onStart")
//                }
//                onSuccess = { currencies ->
//                    Log.e("MY_TAG", "onSuccess $currencies")
//                }
//                onError = { error ->
//                    Log.e("MY_TAG", "onError ${error.localizedMessage}")
//                }
//                onTerminate = {
//                    Log.e("MY_TAG", "onTerminate")
//
//                }
//            }
//        }
//    }

    init {

    }

    fun loadUserProfile(){
        viewModelScope.launch {
            userBalanceRepository.loadUserProfile().collect{ userProfile ->
                println("MY_TAG, $userProfile")
            }
        }
    }
}