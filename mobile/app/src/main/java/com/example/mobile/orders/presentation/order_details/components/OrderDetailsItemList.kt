package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun OrderDetailsItemList(
    order: OrderWithOrderItems,
    onOpenItemDetails: (Int) -> Unit,
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        for(item in order.orderItems){
            OrderDetailsListItem(
                item,
                onClick = { item -> onOpenItemDetails(item.id) }
            )
        }
    }
}

@Preview
@Composable
fun OrderDetailsItemListPreview(){
    MobileTheme {

    }
}