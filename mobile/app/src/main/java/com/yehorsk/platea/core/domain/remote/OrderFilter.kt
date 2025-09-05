package com.yehorsk.platea.core.domain.remote

enum class OrderFilter(val status: String?) {
    ALL(null),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready for Pickup"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");
}