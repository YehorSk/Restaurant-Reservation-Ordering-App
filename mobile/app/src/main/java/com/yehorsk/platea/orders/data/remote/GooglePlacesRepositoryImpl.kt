package com.yehorsk.platea.orders.data.remote

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.remote.dto.GooglePredictionsDto
import com.yehorsk.platea.orders.domain.repository.GooglePlacesRepository
import com.yehorsk.platea.orders.domain.service.GooglePlacesApi
import timber.log.Timber
import javax.inject.Inject

class GooglePlacesRepositoryImpl @Inject constructor(
    private val googlePlacesApi: GooglePlacesApi
): GooglePlacesRepository {

    override suspend fun getPredictions(input: String): Result<GooglePredictionsDto, AppError> {
        Timber.d("GooglePlacesRepositoryImpl getPredictions")
        return try{
            val response = googlePlacesApi.getPredictions(input = input)
            Result.Success(response)
        }catch (e: Exception) {
            Timber.d("Prediction error: " + e)
            Result.Error(AppError.INCORRECT_DATA)
        }
    }

}