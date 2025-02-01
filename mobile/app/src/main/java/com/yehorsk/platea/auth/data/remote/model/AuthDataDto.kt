package com.yehorsk.platea.auth.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthDataDto(
    val user: UserDto? = null,
    val token: String? = null,
    val message: String? = ""
)

@Serializable
data class UserDto(
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
    val role: String? = "",
    val address: String? = "",
    val language: String? = "",
    val phone: String? = ""
)

@Serializable
data class ErrorsDto(
    val email: List<String>? = null,
    val password: List<String>? = null,
    val name: List<String>? = null,
    val passwordConfirmation: List<String>? = null
)
