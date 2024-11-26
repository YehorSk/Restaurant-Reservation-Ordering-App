package com.example.mobile.auth.presentation

import androidx.lifecycle.ViewModel
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
open class BaseAuthViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: MainPreferencesRepository,
    val networkConnectivityObserver: ConnectivityObserver,
) : ViewModel(){

    val isNetwork = networkConnectivityObserver.observe()

    val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

}