package com.yehorsk.platea.core.utils

import android.content.Context
import android.provider.Settings
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import timber.log.Timber
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

    fun getSchedule(schedule: String): Map<String, String>{
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

}