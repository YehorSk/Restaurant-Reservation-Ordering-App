package com.yehorsk.platea.core.data.repository

import com.yehorsk.platea.auth.data.remote.model.UserDto
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.remote.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.ProfileService
import com.yehorsk.platea.core.domain.remote.Result
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

    override suspend fun deleteAccount(): Result<List<String>, AppError> {
        return safeCall<String>(
            execute = {
                profileService.deleteAccount()
            },
            onSuccess = {
                mainPreferencesRepository.clearAllTokens()
                withContext(Dispatchers.IO) {
                    mainRoomDatabase.clearAllTables()
                }
            }
        )
    }

}