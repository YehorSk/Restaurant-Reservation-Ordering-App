package com.example.mobile.core.utils

import android.content.Context
import com.example.mobile.R
import com.example.mobile.core.domain.remote.ReservationFilter

fun ReservationFilter.toString(context: Context): String {
    val resId = when (this) {
        ReservationFilter.ALL -> R.string.filter_all_reservations
        ReservationFilter.PENDING -> R.string.filter_pending
        ReservationFilter.CONFIRMED -> R.string.filter_confirmed
        ReservationFilter.CANCELLED -> R.string.filter_cancelled
    }
    return context.getString(resId)
}