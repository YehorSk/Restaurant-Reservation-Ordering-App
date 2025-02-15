package com.yehorsk.platea.core.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import timber.log.Timber

@Serializable
data class ValidationErrorsDto(
    val message: String,
    val errors: Map<String, List<String>>
)

fun getCredentialErrors(errors: String): ValidationErrorsDto {
    return try {
        val jsonElement = Json.parseToJsonElement(errors)
        if (jsonElement is JsonObject) {
            if ("errors" in jsonElement) {
                Json.decodeFromString<ValidationErrorsDto>(errors)
            } else {
                val message = (jsonElement["message"] as? JsonPrimitive)?.content ?: "Unknown error"
                ValidationErrorsDto(message, emptyMap())
            }
        } else {
            ValidationErrorsDto("Invalid error format", emptyMap())
        }
    } catch (e: Exception) {
        Timber.d("Error decoding validation errors: $e")
        ValidationErrorsDto("Error decoding response", emptyMap())
    }
}