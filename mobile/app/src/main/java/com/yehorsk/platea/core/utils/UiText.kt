package com.yehorsk.platea.core.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource


sealed interface UiText {

    data class DynamicString(
        val value: String
    ): UiText

    @Stable
    data class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText{
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is StringResource) return false

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + args.contentHashCode()
            return result
        }

    }

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id, args)
    }
}