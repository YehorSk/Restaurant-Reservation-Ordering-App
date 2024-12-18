package com.example.mobile.core.domain

enum class AppError: Error{
    NO_INTERNET,
    SERIALIZATION_ERROR,
    UNAUTHORIZED,
    TIMEOUT,
    HTTP_ERROR,
    UNKNOWN_ERROR
}