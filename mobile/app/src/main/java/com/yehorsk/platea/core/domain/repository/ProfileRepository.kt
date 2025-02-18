package com.yehorsk.platea.core.domain.repository

import com.yehorsk.platea.auth.data.remote.model.UserDto
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result

interface ProfileRepository {

    suspend fun updateProfile(
        name: String,
        address: String,
        phone: String,
        countryCode: String
    ): Result<List<UserDto>, AppError>

    suspend fun deleteAccount(): Result<List<String>, AppError>

}