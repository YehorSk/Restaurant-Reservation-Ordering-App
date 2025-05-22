package com.yehorsk.platea.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    cartRepository: CartRepository,
) : ViewModel() {

    val cartItemCount: StateFlow<Int> = cartRepository.getAmountOfItems()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )
}