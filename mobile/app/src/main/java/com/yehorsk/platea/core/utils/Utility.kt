package com.yehorsk.platea.core.utils

import android.content.Context
import android.provider.Settings
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import com.yehorsk.platea.orders.presentation.create_order.components.TimeItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.collections.contains

@Serializable
data class ValidationErrorsDto(
    val message: String,
    val errors: Map<String, List<String>>
)

object Utility {

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getCountryCodeFromPhoneNumber(phoneNumber: String): String? {
        Timber.d("Received Code $phoneNumber")
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            val number = phoneUtil.parse(phoneNumber, null)
            phoneUtil.getRegionCodeForNumber(number)
        } catch (e: Exception) {
            null
        }
    }

    @Serializable
    data class ScheduleTime(
        val hours: String,
        val isOpen: Boolean
    )

    fun getSchedule(schedule: String): Map<String, ScheduleTime>{
        return Json.decodeFromString(schedule)
    }

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

    fun getDayScheduleTranslation(context: Context, day: String, time:String): String{
        val resId = when(day) {
            "Monday" -> R.string.schedule_monday
            "Tuesday" -> R.string.schedule_tuesday
            "Wednesday" -> R.string.schedule_wednesday
            "Thursday" -> R.string.schedule_thursday
            "Friday" -> R.string.schedule_friday
            "Saturday" -> R.string.schedule_saturday
            "Sunday" -> R.string.schedule_sunday
            else -> R.string.schedule_monday
        }
        return context.getString(resId, time)
    }

    fun getDayTranslation(day: String): Int{
        return when(day) {
            "Monday" -> R.string.monday
            "Tuesday" -> R.string.tuesday
            "Wednesday" -> R.string.wednesday
            "Thursday" -> R.string.thursday
            "Friday" -> R.string.friday
            "Saturday" -> R.string.saturday
            "Sunday" -> R.string.sunday
            else -> R.string.monday
        }
    }

    fun ReservationFilter.toString(context: Context): String {
        val resId = when (this) {
            ReservationFilter.ALL -> R.string.filter_all_reservations
            ReservationFilter.PENDING -> R.string.filter_pending
            ReservationFilter.CONFIRMED -> R.string.filter_confirmed
            ReservationFilter.CANCELLED -> R.string.filter_cancelled
        }
        return context.getString(resId)
    }


    fun getCredentialErrors(errors: String): ValidationErrorsDto {
        return try {
            val jsonElement = Json.parseToJsonElement(errors)
            if (jsonElement is JsonObject) {
                if ("errors" in jsonElement) {
                    Json.decodeFromString<ValidationErrorsDto>(errors)
                } else {
                    val message = (jsonElement["message"] as? JsonPrimitive)?.content ?: "Unknown error"
                    ValidationErrorsDto(message, emptyMap())
                }
            } else {
                ValidationErrorsDto("Invalid error format", emptyMap())
            }
        } catch (e: Exception) {
            Timber.d("Error decoding validation errors: $e")
            ValidationErrorsDto("Error decoding response", emptyMap())
        }
    }

    fun getStartTime(): String {
        val currentTime = LocalDateTime.now().plusMinutes(30)
        return if (currentTime.hour >= 24) {
            "08:00"
        } else {
            val minute = currentTime.minute
            val roundedMinute = when {
                minute < 15 -> 0
                minute < 30 -> 15
                minute < 45 -> 30
                else -> 45
            }
            val roundedTime = currentTime.withMinute(roundedMinute).withSecond(0).withNano(0)
            formatTime(roundedTime)
        }
    }

    fun getEndTime(): String {
        val startTime = if (LocalDateTime.now().hour >= 20) {
            LocalDateTime.now().withHour(8).withMinute(30).withSecond(0).withNano(0)
        } else {
            val minute = LocalDateTime.now().minute
            val roundedMinute = getRoundedMinute(minute)
            LocalDateTime.now()
                .withMinute(roundedMinute)
                .withSecond(0)
                .withNano(0)
                .plusMinutes(60)
        }
        return formatTime(startTime)
    }

    fun getRoundedMinute(minute: Int): Int = when {
        minute < 15 -> 0
        minute < 30 -> 15
        minute < 45 -> 30
        else -> 45
    }

    fun formatTime(time: LocalDateTime): String {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun formatStringTime(time: String): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val localTime = LocalTime.parse(time, formatter)
        val localDateTime = LocalDateTime.of(LocalDate.now(), localTime)
        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getDayName(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercaseChar() }
    }

    fun generateTimeSlots(intervalMinutes: Int = 15, date: String, startTimeSchedule: Int, endTimeSchedule: Int): List<TimeItem> {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        Timber.d("Generate ${LocalDate.now()} - $date = ${(LocalDate.now().toString() == date)}")
        var time = if(LocalDate.now().toString() == date){
            LocalTime.now().plusHours(2)
        }else{
            LocalTime.of(startTimeSchedule + 2, 0)
        }

        val roundedNow = time.withMinute((time.minute / intervalMinutes) * intervalMinutes).withSecond(0).withNano(0)

        val endTime = LocalTime.of(endTimeSchedule-2, 0)
        val timeSlots = mutableListOf<TimeItem>()

        var currentStart = roundedNow
        while (currentStart.plusMinutes(30).isBefore(endTime) || currentStart.plusMinutes(30) == endTime) {
            val currentEnd = currentStart.plusMinutes(30)
            timeSlots.add(TimeItem(currentStart.format(timeFormatter), currentEnd.format(timeFormatter)))
            currentStart = currentStart.plusMinutes(intervalMinutes.toLong())
        }

        return timeSlots
    }

}