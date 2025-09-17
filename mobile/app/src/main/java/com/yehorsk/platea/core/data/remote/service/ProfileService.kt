package com.yehorsk.platea.core.data.remote.service

import com.yehorsk.platea.auth.data.remote.models.UserDto
import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileService {

    @POST("update-profile")
    @FormUrlEncoded
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("country_code") countryCode: String,
    ): ResponseDto<UserDto>

    @POST("delete-account")
    suspend fun deleteAccount(): ResponseDto<String>

}