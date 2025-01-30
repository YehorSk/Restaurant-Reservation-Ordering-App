package com.yehorsk.platea.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
open class BaseAuthViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: MainPreferencesRepository,
    val networkConnectivityObserver: ConnectivityObserver,
) : ViewModel(){

    val isNetwork = networkConnectivityObserver
        .observe()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

}