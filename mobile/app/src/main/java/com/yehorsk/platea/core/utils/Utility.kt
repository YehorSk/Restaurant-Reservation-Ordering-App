package com.yehorsk.platea.core.utils

import android.content.Context
import android.provider.Settings
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.serialization.json.Json
import timber.log.Timber

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

}