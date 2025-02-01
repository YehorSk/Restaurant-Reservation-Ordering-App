package com.yehorsk.platea.core.utils

import android.content.Context
import android.provider.Settings

object Utility {

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

}