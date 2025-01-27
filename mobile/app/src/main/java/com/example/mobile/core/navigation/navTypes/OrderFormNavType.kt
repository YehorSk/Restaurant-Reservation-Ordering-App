package com.example.mobile.core.navigation.navTypes


import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.mobile.orders.presentation.OrderForm
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object OrderFormNavType {

    val OrderFormType = object : NavType<OrderForm>(
        isNullableAllowed = false
    ){
        override fun get(
            bundle: Bundle,
            key: String
        ): OrderForm? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): OrderForm {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: OrderForm
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: OrderForm): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }

}