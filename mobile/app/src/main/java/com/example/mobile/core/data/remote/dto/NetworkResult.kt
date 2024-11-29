package com.example.mobile.core.data.remote.dto

sealed class NetworkResult<out T> {
    class Success<out T>(val data: T, val message: String?) : NetworkResult<T>()
    class Error<out T>(val code: Int = 400, val message: String?) : NetworkResult<T>()
}
