package com.yehorsk.platea.core.data.remote.dto

import com.yehorsk.platea.auth.data.remote.models.ErrorsDto
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val status: String?,
    val message: String?,
    val data: List<T>? = null,
    val errors: ErrorsDto? = null
)
