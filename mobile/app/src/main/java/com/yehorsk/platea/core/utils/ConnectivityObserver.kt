package com.yehorsk.platea.core.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val isAvailable: Boolean

    fun observe(): Flow<Boolean>

}