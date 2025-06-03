package com.yehorsk.platea.core.data.remote.service

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.utils.Utility.getCredentialErrors
import com.yehorsk.platea.core.utils.Utility.getMessageFromJson
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
        Timber.d("ERROR UnknownHostException ${e.message}")
        FirebaseCrashlytics.getInstance().recordException(e)
        val error = AppError.NO_INTERNET
        onFailure(error)
        Result.Error(error = error)
    } catch (e: IOException) {
        Timber.d("ERROR IOException ${e.message}")
        FirebaseCrashlytics.getInstance().recordException(e)
        val error = AppError.NO_INTERNET
        onFailure(error)
        Result.Error(error = error)
    } catch (e: SerializationException) {
        Timber.d("ERROR SerializationException")
        FirebaseCrashlytics.getInstance().recordException(e)
        val error = AppError.SERIALIZATION_ERROR
        onFailure(error)
        Result.Error(error = error)
    } catch (e: HttpException) {
        Timber.d("ERROR HttpException - ${e.code()} ${e.response()!!.errorBody()}")
        val error = when (e.code()) {
            401 -> AppError.UNAUTHORIZED
            408 -> AppError.TIMEOUT
            422 -> {
                val errorBodyString = getCredentialErrors(e.response()?.errorBody()?.string()!!)
                Timber.d("ERROR $errorBodyString")
                AppError.IncorrectData(
                    validationErrors = errorBodyString,
                    message = errorBodyString.message
                )
            }
            409 -> {
                Timber.d("ERROR ${e.response()?.errorBody()!!}")
                val errorBodyString = getMessageFromJson(e.response()?.errorBody()?.string()!!)
                AppError.CONFLICT(message = errorBodyString)
            }
            in 500..599 -> {
                FirebaseCrashlytics.getInstance().recordException(e)
                AppError.SERVER_ERROR
            }
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