package com.yehorsk.platea.reservations.domain.models

data class Reservation(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val clientId: Int,
    val tableId: Int,
    val timeSlotId: Int,
    val partySize: Int,
    val date: String,
    val status: String,
    val code: String,
    val orderCode: String? = null,
    val specialRequest: String? = null,
    val phone: String,
    val tableNumber: String,
    val startTime: String,
)
