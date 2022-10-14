package com.test.currencyexchanger.core

fun interface ErrorHandler {
    fun process(e: Throwable): Throwable
}