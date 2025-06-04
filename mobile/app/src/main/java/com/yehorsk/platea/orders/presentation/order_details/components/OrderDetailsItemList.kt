package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.presentation.order_details.components.OrderDetailsListItem
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun OrderDetailsItemList(
    order: Order,
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