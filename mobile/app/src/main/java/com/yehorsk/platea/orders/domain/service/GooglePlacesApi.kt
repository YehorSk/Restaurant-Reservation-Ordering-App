package com.yehorsk.platea.orders.domain.service

import com.yehorsk.platea.BuildConfig.MAPS_KEY
import com.yehorsk.platea.orders.data.remote.dto.GooglePredictionsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("https://maps.googleapis.com/maps/api/place/autocomplete/json")
    suspend fun getPredictions(
        @Query("key") key: String = MAPS_KEY,
        @Query("types") types: String = "address",
        @Query("input") input: String
    ): GooglePredictionsDto

}