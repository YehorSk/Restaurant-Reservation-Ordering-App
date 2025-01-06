package com.example.mobile.core.data.remote

import com.example.mobile.core.data.remote.dto.ResponseDto
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> ResponseDto<T>,
    onSuccess: (List<T>) -> Unit = {},
    onFailure: (AppError) -> Unit = {}
): Result<List<T>, AppError> {
    return try {
        val response = execute()
        onSuccess(response.data!!)
        Result.Success(data = response.data, message = response.message)
    } catch (e: IOException) {
        val error = AppError.NO_INTERNET
        onFailure(error)
        Result.Error(error = error)
    } catch (e: SerializationException) {
        val error = AppError.SERIALIZATION_ERROR
        onFailure(error)
        Result.Error(error = error)
    } catch (e: HttpException) {
        val error = when (e.code()) {
            401 -> AppError.UNAUTHORIZED
            408 -> AppError.TIMEOUT
            422 -> {
                AppError.INCORRECT_DATA
            }
            else -> AppError.HTTP_ERROR
        }
        onFailure(error)
        Result.Error(error = error)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        val error = AppError.UNKNOWN_ERROR
        onFailure(error)
        Result.Error(error = error)
    }
}
