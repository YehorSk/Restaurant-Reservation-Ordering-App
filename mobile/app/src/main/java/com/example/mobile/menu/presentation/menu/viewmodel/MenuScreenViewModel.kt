package com.example.mobile.menu.presentation.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.menu.presentation.menu.MenuScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl,
    val cartRepositoryImpl: CartRepositoryImpl,
    val menuDao: MenuDao
) : ViewModel(){

    private val _uiState = MutableStateFlow(MenuScreenUiState())
    val uiState: StateFlow<MenuScreenUiState> = _uiState.asStateFlow()

    val menuUiState: StateFlow<List<MenuWithMenuItems>> = menuDao.getMenuWithMenuItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getMenus()
    }

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

    fun showBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = true)
        }
    }

    fun closeBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = false)
        }
    }

    fun onSearchValueChange(value: String){
        _uiState.update {
            it.copy(searchText = value)
        }
    }

    fun updatePrice(price: Double){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(price = price))
        }
    }

    fun updateQuantity(quantity: Int){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(quantity = quantity))
        }
    }

    fun setMenuItemId(id: String){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(menuItemId = id))
        }
    }

    fun clearForm(){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(
                price = 0.00,
                quantity = 1,
                menuItemId = ""
            ))
        }
    }

    fun addUserCartItem(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update { state ->
                when(val result = cartRepositoryImpl.addUserCartItem(cartForm = _uiState.value.cartForm)){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                            state.copy(
                                internetError = true
                            )
                        }else{
                            state.copy()
                        }
                    }
                    is NetworkResult.Success -> {
                        _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
                        state.copy()
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
            clearForm()
        }
    }

    fun getMenus(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update { state ->
                when(val result = menuRepositoryImpl.getAllMenus()){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                            state.copy(
                                internetError = true,
                            )
                        }else{
                            _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                            state.copy()
                        }
                    }
                    is NetworkResult.Success ->{
                        state.copy(
                            internetError = false
                        )
                    }

                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun setMenu(menu: MenuItemEntity){
        _uiState.update {
            it.copy(currentMenu = menu)
        }
    }
}
