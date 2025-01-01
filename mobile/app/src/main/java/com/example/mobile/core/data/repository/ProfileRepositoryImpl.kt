package com.example.mobile.core.data.repository

import com.example.mobile.auth.data.remote.model.UserDto
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.ProfileService
import com.example.mobile.core.domain.remote.Result
import com.example.mobile.core.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val mainPreferencesRepository: MainPreferencesRepository
): ProfileRepository {

    override suspend fun updateProfile(
        name: String,
        address: String,
        phone: String
    ): Result<List<UserDto>, AppError> {
        return safeCall<UserDto>(
            execute = {
                profileService.updateProfile(name,address,phone)
            },
            onSuccess = { data ->
                mainPreferencesRepository.saveUserPhone(phone)
                mainPreferencesRepository.saveUserAddress(address)
                mainPreferencesRepository.saveUserName(name)
            }
        )
    }
    
}