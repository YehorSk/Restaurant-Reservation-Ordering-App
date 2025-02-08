package com.yehorsk.platea.core.domain.remote

enum class AppError: Error{
    NO_INTERNET,
    SERIALIZATION_ERROR,
    UNAUTHORIZED,
    TIMEOUT,
    HTTP_ERROR,
    UNKNOWN_ERROR,
    INCORRECT_DATA,
    SERVER_ERROR
}