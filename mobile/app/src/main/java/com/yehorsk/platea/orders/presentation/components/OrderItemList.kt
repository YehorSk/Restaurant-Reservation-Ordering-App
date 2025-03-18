package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.cart.data.db.model.CartItemEntity

@Composable
fun OrderItemList(
    items: List<CartItemEntity>
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
