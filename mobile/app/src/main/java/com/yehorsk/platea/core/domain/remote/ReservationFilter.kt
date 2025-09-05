package com.yehorsk.platea.core.domain.remote

enum class ReservationFilter(val status: String?)  {
    ALL(null),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled")

}
