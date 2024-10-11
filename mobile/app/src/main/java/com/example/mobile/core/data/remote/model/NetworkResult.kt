package com.example.mobile.core.data.remote.model

sealed class NetworkResult<T : Any> {
    class Success<T: Any>(val status: String?, val data: T, val message: String?) : NetworkResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
}
