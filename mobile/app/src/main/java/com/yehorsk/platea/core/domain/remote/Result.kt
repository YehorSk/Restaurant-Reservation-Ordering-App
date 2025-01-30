package com.yehorsk.platea.core.domain.remote

typealias DomainError = Error

sealed interface Result<out D, out E: Error>{
    data class Success<out D>(val data: D, val message: String? = null): Result<D, Nothing>
    data class Error<out E: DomainError>(val error: E): Result<Nothing, E>
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error = error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T, String?) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data,message)
            this
        }
    }
}
inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>