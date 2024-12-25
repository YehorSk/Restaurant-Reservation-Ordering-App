package com.example.mobile.orders.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobile.orders.data.remote.dto.ReservationDto

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
        code = this.code
    )
}
