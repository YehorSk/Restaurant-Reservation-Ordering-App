package com.yehorsk.platea.core.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun cleanError(error: String?) = error?.replace("[", "")?.replace("]", "").orEmpty()

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
    val formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy")
    return localDate.format(formatter)
}

fun formatDateTimeWithoutYear(input: String): String {
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
