package com.yehorsk.platea.core.utils

import android.content.Context
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.AppError

fun AppError.toString(context: Context): String {
    val resId = when (this) {
        AppError.NO_INTERNET -> R.string.error_no_internet
        AppError.SERIALIZATION_ERROR -> R.string.error_serialization
        AppError.UNAUTHORIZED -> R.string.error_unauthorized
        AppError.TIMEOUT -> R.string.error_request_timeout
        AppError.HTTP_ERROR -> R.string.error_http
        AppError.UNKNOWN_ERROR -> R.string.error_unknown
        is AppError.IncorrectData -> R.string.error_incorrect_data
        AppError.SERVER_ERROR -> R.string.error_server_error
        is AppError.CONFLICT -> R.string.conflict_error
        AppError.CANCELLED -> R.string.error_cancelled
        AppError.NO_CREDENTIAL -> R.string.error_no_credential
    }
    return context.getString(resId)
}