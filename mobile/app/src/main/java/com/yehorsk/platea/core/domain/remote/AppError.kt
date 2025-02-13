package com.yehorsk.platea.core.domain.remote

import com.yehorsk.platea.core.utils.ValidationErrorsDto

sealed class AppError(val details: ValidationErrorsDto? = null): Error {
    object UNAUTHORIZED : AppError()
    object TIMEOUT : AppError()
    object SERVER_ERROR : AppError()
    object NO_INTERNET : AppError()
    object UNKNOWN_ERROR : AppError()
    object SERIALIZATION_ERROR : AppError()
    object HTTP_ERROR : AppError()

    data class IncorrectData(val validationErrors: ValidationErrorsDto?) :
        AppError(validationErrors)
}