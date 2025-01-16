package com.example.mobile.orders.data.remote

import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import com.example.mobile.orders.data.remote.dto.GooglePredictionsDto
import com.example.mobile.orders.domain.repository.GooglePlacesRepository
import com.example.mobile.orders.domain.service.GooglePlacesApi
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