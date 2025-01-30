package com.yehorsk.platea.orders.domain.repository

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.remote.dto.GooglePredictionsDto

interface GooglePlacesRepository {

    suspend fun getPredictions(input: String): Result<GooglePredictionsDto, AppError>

}