package com.example.mobile.orders.presentation.order_details

import androidx.lifecycle.ViewModel
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepositoryImpl: OrderRepositoryImpl
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl){

}