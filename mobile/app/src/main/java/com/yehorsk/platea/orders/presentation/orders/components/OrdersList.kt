package com.yehorsk.platea.orders.presentation.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.core.presentation.components.ListHeader
import com.yehorsk.platea.core.utils.Utility.groupOrdersByDate
import com.yehorsk.platea.orders.data.db.model.OrderEntity


@Composable
fun OrdersList(
    modifier: Modifier = Modifier,
    orders: List<OrderEntity>,
    onGoToOrderDetails: (Int) -> Unit,
    showStatus: Boolean = false,
){

    val ordersGrouped = groupOrdersByDate(orders)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        items(orders, key = { it.id }){ order ->
//            OrderListItem(
//                orderEntity = order,
//                onGoToOrderDetails = { onGoToOrderDetails(it) },
//                showStatus = showStatus
//            )
//            HorizontalDivider()
//        }
        itemsIndexed(ordersGrouped, key = { _, group -> group.title }){ _, group ->
            ListHeader(
                title = group.title
            )
            var i = 0;
            Column {
                group.orders.forEach{ item ->
                    OrderListItem(
                        orderEntity = item,
                        onGoToOrderDetails = { onGoToOrderDetails(it) },
                        showStatus = showStatus
                    )
                    if(i++ != group.orders.size -1) HorizontalDivider()
                }
            }
        }
    }
}