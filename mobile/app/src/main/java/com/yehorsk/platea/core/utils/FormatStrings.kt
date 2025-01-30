package com.yehorsk.platea.core.utils

import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun cleanError(error: String?) = error?.replace("[", "")?.replace("]", "").orEmpty()

//fun parseHttpResponse(responseBody: String): HttpResponse {
//    return try {
//        if (responseBody.isBlank()) {
//            Timber.d("Response body is blank or empty")
//            HttpResponse(status = 422, message = "Empty response", authDataDto = null, errorsDto = null)
//        } else {
//            val json = Json { ignoreUnknownKeys = true }
//            json.decodeFromString<HttpResponse>(responseBody)
//        }
//    } catch (e: Exception) {
//        Timber.d("Error parsing HttpResponse $e")
//        HttpResponse(status = 422, message = "Invalid response format", authDataDto = null, errorsDto = null)
//    }
//}

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

fun formatOrderDateTime(input: String): String {
    val zonedDateTime = ZonedDateTime.parse(input)
    val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd-MMM., yyy HH:mm")
    return localDateTime.format(formatter)
}

fun formatTime(input: String): String {
    val parser = DateTimeFormatter.ofPattern("HH:mm:ss")
    val localDate = LocalTime.parse(input, parser)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDate.format(formatter)
}

fun formatDateTime(input: String): String {
    val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(input, parser)
    val formatter = DateTimeFormatter.ofPattern("MMMM dd")
    return localDate.format(formatter)
}

fun formatMonth(input: String): String {
    val parser = DateTimeFormatter.ofPattern("yyyy-MM")
    val yearMonth = YearMonth.parse(input, parser)
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    return yearMonth.format(formatter)
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