package com.example.mobile.core.utils

import android.content.Context
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.R

fun AppError.toString(context: Context): String {
    val resId = when (this) {
        AppError.NO_INTERNET -> R.string.error_no_internet
        AppError.SERIALIZATION_ERROR -> R.string.error_serialization
        AppError.UNAUTHORIZED -> R.string.error_unauthorized
        AppError.TIMEOUT -> R.string.error_request_timeout
        AppError.HTTP_ERROR -> R.string.error_http
        AppError.UNKNOWN_ERROR -> R.string.error_unknown
        AppError.INCORRECT_DATA -> R.string.error_incorrect_data
    }
    return context.getString(resId)
}