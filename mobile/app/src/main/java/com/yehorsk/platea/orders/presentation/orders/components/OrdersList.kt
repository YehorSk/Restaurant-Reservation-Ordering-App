package com.yehorsk.platea.orders.presentation.orders.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.core.presentation.components.ListHeader
import com.yehorsk.platea.orders.domain.models.SectionedOrders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrdersList(
    modifier: Modifier = Modifier,
    orders: List<SectionedOrders>,
    onGoToOrderDetails: (Int) -> Unit,
    showStatus: Boolean = false,
){
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        orders.forEachIndexed { index, (date, orders) ->
            stickyHeader{
                ListHeader(
                    title = date.asString()
                )
            }
            itemsIndexed(orders, key = { _, order -> order.id }){ index, order ->
                OrderListItem(
                    order = order,
                    onGoToOrderDetails = { onGoToOrderDetails(it) },
                    showStatus = showStatus
                )
                HorizontalDivider()
            }
        }
    }
}