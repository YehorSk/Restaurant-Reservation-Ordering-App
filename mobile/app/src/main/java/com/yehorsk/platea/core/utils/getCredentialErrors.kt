package com.yehorsk.platea.core.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber

@Serializable
data class ValidationErrorsDto(
    val message: String,
    val errors: Map<String, List<String>>
)

fun getCredentialErrors(errors: String): ValidationErrorsDto {
    return try {
        Json.decodeFromString<ValidationErrorsDto>(errors)
    } catch (e: Exception) {
        Timber.d("Error decoding validation errors: $e")
        ValidationErrorsDto("Error decoding response", emptyMap())
    }
}