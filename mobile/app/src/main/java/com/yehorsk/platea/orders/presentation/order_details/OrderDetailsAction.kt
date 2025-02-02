package com.yehorsk.platea.orders.presentation.order_details

import kotlinx.serialization.Serializable

@Serializable
data class Status(val status: String)

sealed interface OrderDetailsAction {

    data class RepeatOrder(val id: String): OrderDetailsAction

    data class UserCancelOrder(val id: String): OrderDetailsAction

    data class SetCancelledStatus(val id: String, val status: Status = Status("Cancelled")): OrderDetailsAction

    data class SetCompletedStatus(val id: String, val status: Status = Status("Completed")): OrderDetailsAction

    data class SetConfirmedStatus(val id: String, val status: Status = Status("Confirmed")): OrderDetailsAction

    data class SetPreparingStatus(val id: String, val status: Status = Status("Preparing")): OrderDetailsAction

    data class SetReadyForPickupStatus(val id: String, val status: Status = Status("Ready for Pickup")): OrderDetailsAction

}