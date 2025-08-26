package com.yehorsk.platea.core.utils

import android.content.Context
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.presentation.create_order.components.TimeItem
import com.yehorsk.platea.reservations.domain.models.Reservation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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

    @Serializable
    data class ApiResponse(
        @SerialName("status") val status: Int,
        @SerialName("message") val message: String,
        @SerialName("data") val data: String
    )

    fun getMessageFromJson(jsonString: String): String {
        return try {
            val apiResponse = Json.decodeFromString<ApiResponse>(jsonString)
            apiResponse.message
        } catch (e: Exception) {
            "Error decoding JSON: ${e.message}"
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

    fun generateTimeSlots(intervalMinutes: Int = 15, todayDate: String = LocalDate.now().toString(), date: String, startTimeSchedule: Int, endTimeSchedule: Int): List<TimeItem> {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val now = LocalTime.now()
        val today = todayDate == date

        val startTime = LocalTime.of(startTimeSchedule, 0)
        val endTime = LocalTime.of(endTimeSchedule - 2, 0)

        if (today && now.isAfter(endTime)) return emptyList()

        val initialTime = when {
            today && now.isBefore(startTime) -> startTime.plusHours(2)
            today -> maxOf(now.plusHours(2), startTime)
            else -> startTime.plusHours(2)
        }

        val roundedStart = initialTime.withMinute((initialTime.minute / intervalMinutes) * intervalMinutes).withSecond(0).withNano(0)

        val timeSlots = mutableListOf<TimeItem>()
        var currentStart = roundedStart

        while (currentStart.plusMinutes(30).isBefore(endTime) || currentStart.plusMinutes(30) == endTime) {
            val currentEnd = currentStart.plusMinutes(30)
            timeSlots.add(TimeItem(currentStart.format(timeFormatter), currentEnd.format(timeFormatter)))
            currentStart = currentStart.plusMinutes(intervalMinutes.toLong())
        }

        return timeSlots
    }

    fun getOrderColorIndicator(status: String, date: String, endTime: String): Color{
        val orderDate = LocalDate.parse(date)
        Timber.d("OrderDate: $orderDate ")
        return if(status in arrayOf("Confirmed", "Preparing")){
            if(orderDate == LocalDate.now()){
                val now = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val parsedEndTime = LocalTime.parse(endTime, formatter)

                val minutesLeft = ChronoUnit.MINUTES.between(now, parsedEndTime)

                when {
                    minutesLeft <= 0 -> Color.Transparent
                    minutesLeft <= 60 -> Color.Red
                    minutesLeft <= 180 -> Color.Transparent // Color(0xFFFFA500)
                    minutesLeft <= 720 -> Color.Transparent // Color.Yellow
                    else -> Color.Transparent
                }
            }else if (orderDate.isAfter(LocalDate.now())) {
                Color.Transparent
            } else {
                Color.Transparent
            }
        }else{
            Color.Transparent
        }
    }


    enum class FilterTypeOrder(@StringRes val label: Int) {
        TODAY(R.string.today),
        TODAY_HISTORY(R.string.today_history),
        TOMORROW(R.string.tomorrow),
        LATER(R.string.later),
        HISTORY(R.string.history)
    }

    enum class FilterTypeReservation(@StringRes val label: Int) {
        TODAY(R.string.today),
        TOMORROW(R.string.tomorrow),
        LATER(R.string.later),
        HISTORY(R.string.history)
    }

    data class SectionedReservation(
        @StringRes val title: Int,
        val reservations: List<Reservation>
    )

    data class SectionedOrders(
        @StringRes val title: Int,
        val orders: List<Order>
    )

    fun groupOrdersByDate(orders: List<Order>): List<SectionedOrders>{
        val today = LocalDate.now()
        val time = LocalTime.now()

        val grouped = FilterTypeOrder.entries.map { type ->
            val filteredOrders = orders.filter { order ->
                val orderDate = order.date?.let { LocalDate.parse(it) }
                val orderEndTime = order.endTime.let { LocalTime.parse(it) }
                when (type) {
                    FilterTypeOrder.TODAY -> (orderDate == today && orderEndTime.isAfter(time) && order.status !in arrayOf("Cancelled", "Completed", "Rejected"))
                    FilterTypeOrder.TODAY_HISTORY -> (orderDate == today && (orderEndTime.isBefore(time) || order.status in arrayOf("Cancelled", "Completed", "Rejected")))
                    FilterTypeOrder.TOMORROW -> orderDate == today.plusDays(1)
                    FilterTypeOrder.LATER -> orderDate!!.isAfter(today.plusDays(1))
                    FilterTypeOrder.HISTORY -> orderDate!!.isBefore(today)
                }
            }
            SectionedOrders(title = type.label, orders = filteredOrders)
        }
        return grouped.filter { it.orders.isNotEmpty() }
    }

    fun groupReservationsByDate(reservations: List<Reservation>): List<SectionedReservation>{
        val today = LocalDate.now()

        val grouped = FilterTypeReservation.entries.map { type ->
            val filteredOrders = reservations.filter { reservation ->
                val reservationDate = reservation.date.let { LocalDate.parse(it) } ?: return@filter false
                when (type) {
                    FilterTypeReservation.TODAY -> reservationDate == today
                    FilterTypeReservation.TOMORROW -> reservationDate == today.plusDays(1)
                    FilterTypeReservation.LATER -> reservationDate.isAfter(today.plusDays(1))
                    FilterTypeReservation.HISTORY -> reservationDate.isBefore(today)
                }
            }

            SectionedReservation(title = type.label, reservations = filteredOrders)
        }
        return grouped.filter { it.reservations.isNotEmpty() }
    }

}