package com.yehorsk.platea.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.notifications.data.dao.NotificationDao
import com.yehorsk.platea.notifications.data.db.model.NotificationEntity
import com.yehorsk.platea.notifications.data.remote.NotificationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val notificationRepositoryImpl: NotificationRepositoryImpl,
    val notificationDao: NotificationDao
): ViewModel(){

    protected val _uiState = MutableStateFlow(NotificationScreenUiState())
    val uiState: StateFlow<NotificationScreenUiState> = _uiState.asStateFlow()

    val notificationUiState: StateFlow<List<NotificationEntity>> = notificationDao.getNotifications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: ReceiveChannel<SideEffect> = _sideEffectChannel

    val isNetwork = networkConnectivityObserver
        .observe()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    protected fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    init {
        getNotifications()
    }

    fun getNotifications(){
        viewModelScope.launch{
            notificationRepositoryImpl.getAllNotifications()
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

}