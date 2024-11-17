package com.dicoding.picodiploma.loginwithanimation.data.pref

sealed class Result<out R> private constructor() {
    data class DataSuccess<out T>(val data: T) : Result<T>()
    data class DataError(val error: String) : Result<Nothing>()
    data object DataLoading : Result<Nothing>()
}