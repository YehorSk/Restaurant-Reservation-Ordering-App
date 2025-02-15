package com.yehorsk.platea.reservations.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yehorsk.platea.reservations.data.remote.dto.ReservationDto

@Entity("reservation_table")
data class ReservationEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    @ColumnInfo("client_id")
    val clientId: Int,
    @ColumnInfo("table_id")
    val tableId: Int,
    @ColumnInfo("time_slot_id")
    val timeSlotId: Int,
    @ColumnInfo("party_size")
    val partySize: Int,
    val date: String,
    val status: String,
    val code: String,
    @ColumnInfo("special_request")
    val specialRequest: String? = null,
    val phone: String,
    @ColumnInfo("table_number")
    val tableNumber: String,
    @ColumnInfo("start_time")
    val startTime: String
)

fun ReservationEntity.toReservationDto(): ReservationDto{
    return ReservationDto(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        clientId = this.clientId,
        tableId = this.tableId,
        timeSlotId = this.timeSlotId,
        partySize = this.partySize,
        date = this.date,
        status = this.status,
        code = this.code,
        tableNumber = this.tableNumber,
        startTime = this.startTime,
        specialRequest = this.specialRequest,
        phone = this.phone
    )
}
