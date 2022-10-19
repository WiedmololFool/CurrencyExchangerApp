package com.test.currencyexchanger.domain.validation

interface Validator<T> {

    @Throws(Exception::class)
    suspend fun validateOrThrow(value: T)
}