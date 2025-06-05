package com.yehorsk.platea.reservations.data.mappers

import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.data.remote.dto.ReservationDto
import com.yehorsk.platea.reservations.domain.models.Reservation

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
        phone = this.phone,
        orderCode = this.orderCode
    )
}

fun ReservationEntity.toReservation(): Reservation{
    return Reservation(
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
        phone = this.phone,
        orderCode = this.orderCode
    )
}

fun ReservationDto.toReservation(): Reservation{
    return Reservation(
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
        phone = this.phone,
        orderCode = this.orderCode
    )
}

