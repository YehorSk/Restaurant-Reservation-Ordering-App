package com.yehorsk.platea.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
open class BaseAuthViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: MainPreferencesRepository,
    val networkConnectivityObserver: ConnectivityObserver
) : ViewModel(){

    val isNetwork = MutableStateFlow<Boolean>(networkConnectivityObserver.isAvailable)

    init{
        viewModelScope.launch{
            networkConnectivityObserver.observe().collect { status ->
                isNetwork.value = status
            }
        }
        viewModelScope.launch{
            Firebase.messaging.subscribeToTopic("all").await()
        }
    }

}