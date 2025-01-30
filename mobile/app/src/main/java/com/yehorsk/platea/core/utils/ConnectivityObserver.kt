package com.yehorsk.platea.core.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Boolean>

}