package com.yehorsk.platea.core.utils

import android.content.Context
import com.yehorsk.platea.R

fun statusToString(status: String, context: Context): String{
    return when(status) {
        "Pending" -> context.getString(R.string.status_pending)
        "Confirmed" -> context.getString(R.string.status_confirmed)
        "Preparing" -> context.getString(R.string.status_preparing)
        "Ready for Pickup" -> context.getString(R.string.status_ready_pickup)
        "On the Way" -> context.getString(R.string.status_on_the_way)
        "Completed" -> context.getString(R.string.status_completed)
        "Cancelled" -> context.getString(R.string.status_cancelled)
        "Rejected" -> context.getString(R.string.status_rejected)
        "Ready for Dine-In" -> context.getString(R.string.status_ready_dinein)
        else -> context.getString(R.string.status_pending)
    }
}