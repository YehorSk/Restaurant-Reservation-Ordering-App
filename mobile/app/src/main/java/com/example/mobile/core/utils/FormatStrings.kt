package com.example.mobile.core.utils

import com.example.mobile.auth.data.remote.model.HttpResponse
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun cleanError(error: String?) = error?.replace("[", "")?.replace("]", "").orEmpty()

fun parseHttpResponse(responseBody: String): HttpResponse {
    return try {
        if (responseBody.isBlank()) {
            Timber.d("Response body is blank or empty")
            HttpResponse(status = 422, message = "Empty response", data = null, errors = null)
        } else {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<HttpResponse>(responseBody)
        }
    } catch (e: Exception) {
        Timber.d("Error parsing HttpResponse $e")
        HttpResponse(status = 422, message = "Invalid response format", data = null, errors = null)
    }
}

fun formattedPrice(price: Double): String{
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        val fractionDigits = when {
            price > 1000 -> 0
            price in 2f..999f -> 2
            else -> 3
        }
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = 0
    }
    return formatter.format(price)
}

fun formatDateTime(input: String): String {
    val zonedDateTime = ZonedDateTime.parse(input)

    val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern("dd-MMM., yyy HH:mm")
    return localDateTime.format(formatter)
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}