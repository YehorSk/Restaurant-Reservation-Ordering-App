package com.example.mobile.orders.presentation.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.ui.theme.MobileTheme


@Composable
fun OrdersList(
    modifier: Modifier = Modifier,
    orders: List<OrderEntity>,
    onGoToOrderDetails: (Int) -> Unit
){

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(orders){ order ->
            OrderListItem(
                orderEntity = order,
                onGoToOrderDetails = { onGoToOrderDetails(it) }
            )
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
fun OrdersListPreview(){
    MobileTheme {

    }
}