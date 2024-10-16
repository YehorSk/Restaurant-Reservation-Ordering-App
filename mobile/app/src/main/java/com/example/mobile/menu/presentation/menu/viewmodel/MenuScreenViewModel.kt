package com.example.mobile.menu.presentation.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.menu.data.remote.model.Menu
import com.example.mobile.menu.data.remote.model.MenuItem
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow(ClientMainUiState())
    val uiState: StateFlow<ClientMainUiState> = _uiState.asStateFlow()

    val menuUiState: StateFlow<List<MenuWithMenuItems>> = menuDao.getMenuWithMenuItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _cartForm = MutableStateFlow(CartForm())
    val cartForm: StateFlow<CartForm> = _cartForm.asStateFlow()

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getMenus()
    }

    fun updatePrice(price: Double){
        Timber.d("price $price")
        Timber.d("Cart ${_cartForm.value}")
        _cartForm.update {
            it.copy(price = price)
        }
    }

    fun updateQuantity(quantity: Int){
        Timber.d("Quantity $quantity")
        Timber.d("Cart ${_cartForm.value}")
        _cartForm.update {
            it.copy(quantity = quantity)
        }
    }

    fun setMenuItemId(id: String){
        _cartForm.update {
            it.copy(
                menuItemId = id
            )
        }
    }

    fun clearForm(){
        _cartForm.update {
            it.copy(
                price = 0.00,
                quantity = 1,
                menuItemId = ""
            )
        }
    }

    fun addUserCartItem(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                Timber.d(_cartForm.value.toString())
                when(val result = cartRepositoryImpl.addUserCartItem(cartForm = _cartForm.value)){
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
            _isLoading.value = false
            clearForm()
        }
    }

    fun getMenus(){
        viewModelScope.launch {
            _isLoading.value = true
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
                        menuDao.insert(result.data)
                        state.copy(
                            internetError = false
                        )
                    }

                }
            }
            _isLoading.value = false
        }
    }

    fun setMenu(menu: MenuItem){
        _uiState.update {
            it.copy(currentMenu = menu)
        }
    }
}

data class ClientMainUiState(
    val currentMenu: MenuItem? = null,
    val internetError: Boolean = false,
)

