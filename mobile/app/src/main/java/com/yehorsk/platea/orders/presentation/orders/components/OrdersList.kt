package com.yehorsk.platea.orders.presentation.orders.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.yehorsk.platea.core.presentation.components.ListHeader
import com.yehorsk.platea.core.utils.Utility.SectionedOrders
import com.yehorsk.platea.core.utils.Utility.groupOrdersByDate
import com.yehorsk.platea.orders.data.db.model.OrderEntity


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrdersList(
    modifier: Modifier = Modifier,
    orders: List<OrderEntity>,
    onGoToOrderDetails: (Int) -> Unit,
    showStatus: Boolean = false,
){
    val listState = rememberLazyListState()
    var ordersGrouped by remember { mutableStateOf<List<SectionedOrders>>(emptyList()) }
    LaunchedEffect(orders) {
        ordersGrouped = groupOrdersByDate(orders)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ordersGrouped.forEach{ group ->
            stickyHeader{
                ListHeader(
                    title = group.title
                )
            }
            items(group.orders, key = { it.id }){ item ->
                OrderListItem(
                    orderEntity = item,
                    onGoToOrderDetails = { onGoToOrderDetails(it) },
                    showStatus = showStatus
                )
                HorizontalDivider()
            }
        }
    }
}