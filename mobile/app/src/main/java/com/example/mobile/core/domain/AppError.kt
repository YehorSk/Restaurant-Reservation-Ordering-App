package com.example.mobile.core.domain

sealed interface AppError: Error{

    enum class NetworkError: AppError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION_ERROR,
        UNKNOWN
    }

    enum class AuthError: AppError{

    }

    enum class CartError: AppError{
        ADD_FAILURE,
        UPDATE_FAILURE,
        DELETE_FAILURE,
    }

    enum class MenuError: AppError{
        ADD_FAILURE,
        UPDATE_FAILURE,
        DELETE_FAILURE,
    }

}