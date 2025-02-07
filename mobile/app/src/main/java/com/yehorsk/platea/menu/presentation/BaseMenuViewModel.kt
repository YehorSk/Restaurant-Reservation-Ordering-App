package com.yehorsk.platea.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.data.remote.CartRepositoryImpl
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems
import com.yehorsk.platea.menu.data.remote.MenuRepositoryImpl
import com.yehorsk.platea.menu.presentation.menu.MenuScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseMenuViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl,
    val cartRepositoryImpl: CartRepositoryImpl,
    val networkConnectivityObserver: ConnectivityObserver,
    val menuDao: MenuDao
) : ViewModel(){

    protected val _uiState = MutableStateFlow(MenuScreenUiState())
    val uiState: StateFlow<MenuScreenUiState> = _uiState.asStateFlow()

    val menuUiState: StateFlow<List<MenuWithMenuItems>> = menuDao.getMenuWithMenuItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val isNetwork = MutableStateFlow<Boolean>(networkConnectivityObserver.isAvailable)

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: ReceiveChannel<SideEffect> = _sideEffectChannel


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchUiState: StateFlow<List<MenuItemEntity>> = _uiState
        .map { it.searchText }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            menuDao.searchItems("%$query%")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        viewModelScope.launch{
            networkConnectivityObserver.observe().collect { status ->
                isNetwork.value = status
            }
        }
        getMenus()
    }

    protected fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun onSearchValueChange(value: String){
        _uiState.update {
            it.copy(searchText = value)
        }
    }

    fun getMenus(){
        viewModelScope.launch {
            setLoadingState(true)
            menuRepositoryImpl.getAllMenus()
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            setLoadingState(false)
        }
    }

}