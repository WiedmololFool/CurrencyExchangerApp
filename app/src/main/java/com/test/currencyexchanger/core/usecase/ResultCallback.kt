package com.test.currencyexchanger.core.usecase

typealias Callback<T> = ResultCallback<T>.() -> Unit

class ResultCallback<T> {
    var onStart: (() -> Unit)? = null
    var onSuccess: ((T) -> Unit)? = null
    var onError: ((error: Throwable) -> Unit)? = null
    var onTerminate: ((isCanceled: Boolean) -> Unit)? = null
}