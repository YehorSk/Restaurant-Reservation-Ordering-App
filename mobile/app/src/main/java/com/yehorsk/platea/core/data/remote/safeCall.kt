package com.yehorsk.platea.core.data.remote

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException
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
    }catch (e: UnknownHostException) {
        Timber.d("ERROR UnknownHostException")
        val error = AppError.UNKNOWN_ERROR
        onFailure(error)
        Result.Error(error = error)
    } catch (e: IOException) {
        Timber.d("ERROR IOException")
        val error = AppError.NO_INTERNET
        onFailure(error)
        Result.Error(error = error)
    } catch (e: SerializationException) {
        Timber.d("ERROR SerializationException")
        val error = AppError.SERIALIZATION_ERROR
        onFailure(error)
        Result.Error(error = error)
    } catch (e: HttpException) {
        Timber.d("ERROR HttpException")
        val error = when (e.code()) {
            401 -> AppError.UNAUTHORIZED
            408 -> AppError.TIMEOUT
            422 -> {
                AppError.INCORRECT_DATA
            }
            in 500..599 -> AppError.SERVER_ERROR
            else -> AppError.UNKNOWN_ERROR
        }
        onFailure(error)
        Result.Error(error = error)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        Timber.d("Error $e")
        val error = AppError.UNKNOWN_ERROR
        onFailure(error)
        Result.Error(error = error)
    }
}