package com.example.mobile.orders.domain.repository

import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import com.example.mobile.orders.data.remote.dto.GooglePredictionsDto

interface GooglePlacesRepository {

    suspend fun getPredictions(input: String): Result<GooglePredictionsDto, AppError>

}