package com.example.mobile.core.utils

import android.content.Context
import com.example.mobile.R
import com.example.mobile.core.domain.remote.OrderFilter

fun OrderFilter.toString(context: Context): String {
    val resId = when (this) {
        OrderFilter.ALL -> R.string.filter_all_orders
        OrderFilter.PENDING -> R.string.filter_pending
        OrderFilter.CONFIRMED -> R.string.filter_confirmed
        OrderFilter.PREPARING -> R.string.filter_preparing
        OrderFilter.READY -> R.string.filter_ready
        OrderFilter.COMPLETED -> R.string.filter_completed
        OrderFilter.CANCELLED -> R.string.filter_cancelled
    }
    return context.getString(resId)
}