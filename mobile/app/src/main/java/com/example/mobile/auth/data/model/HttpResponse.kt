package com.example.mobile.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

object DataSerializer : JsonTransformingSerializer<Data>(Data.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        // If the element is an array, replace it with an empty object
        return if (element is JsonArray) JsonObject(emptyMap()) else element
    }
}

@Serializable
data class HttpResponse(
    val status: Int? = null,
    val message: String?,
    @Serializable(with = DataSerializer::class)
    val data: Data? = null,
    val errors: Errors? = null
)

@Serializable
data class Data(
    val user: User? = null,
    val token: String? = null,
    val message: String? = ""
)

@Serializable
data class User(
    val name: String? = "",
    val email: String? = "",
    @SerialName("email_verified_at")
    val emailVerifiedAt: String? = "",
    @SerialName("updated_at")
    val updatedAt: String? = "",
    @SerialName("created_at")
    val createdAt: String? = "",
    val id: Int? = 0,
    val message: String? = "",
    val role: String? = ""
)

@Serializable
data class Errors(
    val email: List<String>? = listOf(),
    val password: List<String>? = listOf(),
    val name: List<String>? = listOf(),
    val passwordConfirmation: List<String>? = listOf()
)
