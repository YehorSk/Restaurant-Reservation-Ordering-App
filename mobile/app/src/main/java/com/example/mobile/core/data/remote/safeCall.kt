package com.example.mobile.core.data.remote

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.dto.ResponseDto
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    isOnlineFlow: StateFlow<Boolean>,
    execute: () -> ResponseDto<T>,
    onSuccess: (List<T>) -> Unit = {},
    onFailure: () -> Unit = {}
): NetworkResult<List<T>> {
    if (!isOnlineFlow.first()) {
        return NetworkResult.Error(code = 503, message = "No internet connection!")
    }
    return try {
        val response = execute()
        onSuccess(response.data!!)
        NetworkResult.Success(data = response.data, message = response.message)
    } catch (e: SerializationException) {
        NetworkResult.Error(message = "We encountered an issue while processing your request. Please try again later.")
    } catch (e: HttpException) {
        onFailure()
        when (e.code()) {
            401 -> NetworkResult.Error(code = 401, message = "You are not authorized. Please log in again.")
            408 -> NetworkResult.Error(code = 408, message = "The request timed out. Please try again.")
            else -> NetworkResult.Error(code = e.code(), message = "HTTP error: ${e.message()}. Please try again later.")
        }
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        NetworkResult.Error(message = "An unknown error occurred. Please try again.")
    }
}
