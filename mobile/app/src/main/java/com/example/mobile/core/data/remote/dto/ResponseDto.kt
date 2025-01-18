package com.example.mobile.core.data.remote.dto

import com.example.mobile.auth.data.remote.model.ErrorsDto
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val status: String?,
    val message: String?,
    val data: List<T>? = null,
    val errors: ErrorsDto? = null
)
