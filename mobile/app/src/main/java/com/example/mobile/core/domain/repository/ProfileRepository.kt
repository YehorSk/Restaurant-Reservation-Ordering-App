package com.example.mobile.core.domain.repository

import com.example.mobile.auth.data.remote.model.UserDto
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result

interface ProfileRepository {

    suspend fun updateProfile(
        name: String,
        address: String,
        phone: String
    ): Result<List<UserDto>, AppError>

}