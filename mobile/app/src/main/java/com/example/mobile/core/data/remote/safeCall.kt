package com.example.mobile.core.data.remote

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.dto.ResponseDto
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    isOnlineFlow: StateFlow<Boolean>,
    execute: () -> ResponseDto<T>,
    onSuccess: (List<T>) -> Unit = {},
    onFailure: () -> Unit = {}
): NetworkResult<List<T>>{
    if(!isOnlineFlow.first()){
        return NetworkResult.Error(code = 503, message = "No internet connection!")
    }
    val response = try{
        execute()
    }catch (e: SerializationException){
        return NetworkResult.Error(message = "NetworkError.SERIALIZATION_ERROR")
    }catch (e: Exception){
        coroutineContext.ensureActive()
        return NetworkResult.Error(message = "NetworkError.UNKNOWN")
    }
    return responseToResult(
        isOnlineFlow = isOnlineFlow,
        response = response,
        onFailure = onFailure,
        onSuccess = onSuccess
    )
}

suspend inline fun<reified T> responseToResult(
    isOnlineFlow: StateFlow<Boolean>,
    response: ResponseDto<T>,
    onSuccess: (List<T>) -> Unit = {},
    onFailure: () -> Unit = {}
): NetworkResult<List<T>>{
    return try {
        onSuccess(response.data!!)
        NetworkResult.Success(data = response.data, message = response.message)
    } catch (e: HttpException) {
        onFailure()
        when (e.code()) {
            401 -> NetworkResult.Error(code = 401, message = "No User")
            408 -> NetworkResult.Error(code = 408, message = "Request Timeout")
            else -> NetworkResult.Error(code = 520, message = e.message())
        }
    }

}