package com.example.mobile.orders.data.remote.dto

import com.example.mobile.orders.data.db.model.ReservationEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservationDto(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("client_id")
    val clientId: Int,
    @SerialName("table_id")
    val tableId: Int,
    @SerialName("time_slot_id")
    val timeSlotId: Int,
    @SerialName("party_size")
    val partySize: Int,
    val date: String,
    val status: String,
    val code: String,
)

fun ReservationDto.toReservationEntity(): ReservationEntity{
    return ReservationEntity(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        clientId = this.clientId,
        tableId = this.tableId,
        timeSlotId = this.timeSlotId,
        partySize = this.partySize,
        date = this.date,
        status = this.status,
        code = this.code
    )
}