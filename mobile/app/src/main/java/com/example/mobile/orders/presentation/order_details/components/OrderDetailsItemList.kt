package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.Pivot
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun OrderDetailsItemList(
    order: OrderWithOrderItems
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for(item in order.orderItems){
            OrderDetailsListItem(item) {
            }
        }
    }
}

@Preview
@Composable
fun OrderDetailsItemListPreview(){
    MobileTheme {

    }
}