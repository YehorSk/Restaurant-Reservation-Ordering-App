package com.example.mobile.core.data.remote

import com.example.mobile.core.data.remote.model.NetworkResult
import retrofit2.HttpException
import retrofit2.Response

interface ApiHandler {
    suspend fun <T : Any> handleApi(
        token: Boolean,
        isOnline: Boolean,
        execute: suspend () -> T
    ): NetworkResult<T> {
        return if(isOnline){
            try {
                if(!token){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = execute()
                NetworkResult.Success(status = "200", data = result, message = "")
            }catch (e: HttpException){
                if(e.code() == 401){
                    NetworkResult.Error(code = 401, message = "No User")
                }else{
                    NetworkResult.Error(code = 520, message = e.message())
                }
            }
        }else{
            NetworkResult.Error(code = 503, message = "No internet connection!")
        }
    }
}