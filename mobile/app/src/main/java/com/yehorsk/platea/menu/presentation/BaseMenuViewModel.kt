package com.yehorsk.platea.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems
import com.yehorsk.platea.menu.domain.repository.MenuRepository
import com.yehorsk.platea.menu.presentation.menu.MenuScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseMenuViewModel @Inject constructor(
    val menuRepository: MenuRepository,
    val cartRepository: CartRepository,
    val networkConnectivityObserver: ConnectivityObserver
) : ViewModel(){

    private var menuWithMenuItemsJob: Job? = null
    private var favoriteItemsJob: Job? = null

    protected val _uiState = MutableStateFlow(MenuScreenUiState())
    val uiState: StateFlow<MenuScreenUiState> = _uiState
        .onStart {
            observeMenuWithMenuItems()
            observeFavoriteItems()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _uiState.value
        )

    val isNetwork = MutableStateFlow(networkConnectivityObserver.isAvailable)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchUiState: StateFlow<List<MenuItemEntity>> = _uiState
        .map { it.searchText }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            menuRepository.searchItems("%$query%")
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

    private fun observeMenuWithMenuItems(){
        menuWithMenuItemsJob?.cancel()
        menuWithMenuItemsJob = menuRepository
            .getMenuWithMenuItems()
            .onEach { menuWithMenuItems ->
                _uiState.update { it.copy(
                    menuWithMenuItems = menuWithMenuItems
                )}
            }
            .launchIn(viewModelScope)
    }

    private fun observeFavoriteItems(){
        favoriteItemsJob?.cancel()
        favoriteItemsJob = menuRepository
            .getFavoriteItems()
            .onEach { favoriteItems ->
                _uiState.update { it.copy(
                    favoriteItems = favoriteItems
                )}
            }
            .launchIn(viewModelScope)
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
            menuRepository.getAllMenus()
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            setLoadingState(false)
        }
    }

}