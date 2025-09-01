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
    object CANCELLED : AppError()
    object NO_CREDENTIAL : AppError()

    data class IncorrectData(val validationErrors: ValidationErrorsDto?, val message: String? = null) :
        AppError(validationErrors)

    data class CONFLICT(val message: String): AppError()
}