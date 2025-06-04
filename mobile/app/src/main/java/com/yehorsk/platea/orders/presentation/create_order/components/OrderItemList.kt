package com.yehorsk.platea.orders.presentation.create_order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.cart.domain.models.CartItem

@Composable
fun OrderItemList(
    items: List<CartItem>
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        for (item in items) {
            OrderListItem(item) {
            }
        }
    }
}
