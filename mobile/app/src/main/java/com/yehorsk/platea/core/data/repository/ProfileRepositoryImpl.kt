package com.yehorsk.platea.core.data.repository

import com.yehorsk.platea.auth.data.remote.models.UserDto
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.remote.service.ProfileService
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val mainPreferencesRepository: MainPreferencesRepository,
    private val mainRoomDatabase: MainRoomDatabase
): ProfileRepository {

    override suspend fun updateProfile(
        name: String,
        address: String,
        phone: String,
        countryCode: String
    ): Result<List<UserDto>, AppError> {
        return safeCall<UserDto>(
            execute = {
                profileService.updateProfile(name, address, phone, countryCode)
            }
        ).onSuccess { data, _ ->
            mainPreferencesRepository.saveUserPhone(phone)
            mainPreferencesRepository.saveUserAddress(address)
            mainPreferencesRepository.saveUserName(name)
            mainPreferencesRepository.saveUserCountryCode(countryCode) }
    }

    override suspend fun deleteAccount(): Result<List<String>, AppError> {
        return safeCall<String>(
            execute = {
                profileService.deleteAccount()
            }
        ).onSuccess { _, _ ->
            mainPreferencesRepository.clearAllTokens()
            withContext(Dispatchers.IO) {
                mainRoomDatabase.clearAllTables()
            }
        }
    }

}