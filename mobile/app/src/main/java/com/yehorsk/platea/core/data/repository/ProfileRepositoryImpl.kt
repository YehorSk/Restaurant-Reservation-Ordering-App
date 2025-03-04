package com.yehorsk.platea.core.data.repository

import com.yehorsk.platea.auth.data.remote.model.UserDto
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.remote.dto.RestaurantInfoDto
import com.yehorsk.platea.core.data.remote.dto.toRestaurantInfoEntity
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.data.remote.service.ProfileService
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val mainPreferencesRepository: MainPreferencesRepository,
    private val mainRoomDatabase: MainRoomDatabase,
    private val restaurantInfoDao: RestaurantInfoDao
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
            },
            onSuccess = { data ->
                mainPreferencesRepository.saveUserPhone(phone)
                mainPreferencesRepository.saveUserAddress(address)
                mainPreferencesRepository.saveUserName(name)
                mainPreferencesRepository.saveUserCountryCode(countryCode)
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

    override suspend fun getRestaurantInfo(): Result<List<RestaurantInfoDto>, AppError> {
        Timber.d("getRestaurantInfo")
        return safeCall<RestaurantInfoDto>(
            execute = {
                profileService.getRestaurantInfo()
            },
            onSuccess = { data ->
                Timber.d("Restaurant $data")
                restaurantInfoDao.upsertRestaurantInfo(data.first().toRestaurantInfoEntity())
            }
        )
    }

}