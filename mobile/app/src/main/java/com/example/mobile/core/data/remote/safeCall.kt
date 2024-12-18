package com.example.mobile.core.data.remote

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.dto.ResponseDto
import com.example.mobile.core.domain.AppError
import com.example.mobile.core.domain.Result
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> ResponseDto<T>,
    onSuccess: (List<T>) -> Unit = {},
    onFailure: () -> Unit = {}
): Result<List<T>, AppError> {
    return try {
        Timber.d("Starting execution...")
        val response = execute()
        Timber.d("Response received: $response")
        onSuccess(response.data!!)
        Timber.d("onSuccess executed successfully")
        Result.Success(data = response.data, message = response.message)
    }catch (e: IOException) {
        Result.Error(error = AppError.NO_INTERNET)
    } catch (e: SerializationException) {
        Result.Error(error = AppError.SERIALIZATION_ERROR)
    } catch (e: HttpException) {
        onFailure()
        when (e.code()) {
            401 -> Result.Error(error = AppError.UNAUTHORIZED)
            408 -> Result.Error(error = AppError.TIMEOUT)
            else -> Result.Error(error = AppError.HTTP_ERROR)
        }
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        Timber.d("Error $e")
        Result.Error(error = AppError.UNKNOWN_ERROR)
    }
}