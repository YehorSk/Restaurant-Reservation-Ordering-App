package com.yehorsk.platea.core.data.remote.dto

import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantInfoDto(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String,
    @SerialName("opening_hours")
    val openingHours: String
)

fun RestaurantInfoDto.toRestaurantInfoEntity(): RestaurantInfoEntity{
    return RestaurantInfoEntity(
        id = this.id.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        description = this.description,
        address =this.address,
        phone = this.phone,
        email = this.email,
        website = this.website,
        openingHours = this.openingHours
    )
}