package com.example.mobile.core.domain.remote

import com.example.mobile.auth.data.remote.model.UserDto
import com.example.mobile.core.data.remote.dto.ResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileService {

    @POST("update-profile")
    @FormUrlEncoded
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("phone") phone: String
    ): ResponseDto<UserDto>

}