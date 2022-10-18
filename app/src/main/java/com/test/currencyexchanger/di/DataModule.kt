package com.test.currencyexchanger.di

import com.test.currencyexchanger.data.local.SharedPrefUserProfileStorage
import com.test.currencyexchanger.data.local.UserProfileStorage
import com.test.currencyexchanger.data.remote.CurrencyApi
import com.test.currencyexchanger.data.remote.EndPoints
import com.test.currencyexchanger.data.remote.CurrencyDataSourceImpl
import com.test.currencyexchanger.data.remote.CurrencyDataSource
import com.test.currencyexchanger.data.repository.UserBalanceRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    factory {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("apikey", EndPoints.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory {
        get<Retrofit>().create(CurrencyApi::class.java)
    }

    single<CurrencyDataSource> {
        CurrencyDataSourceImpl(currencyApi = get())
    }

    single<UserProfileStorage> {
        SharedPrefUserProfileStorage(context = get())
    }

    single {
        UserBalanceRepository(
            userProfileStorage = get(),
            currencyDataSource = get()
        )
    }
}