package com.yehorsk.platea.orders.presentation.order_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.ActionButton
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.orders.presentation.components.OrderDetailsItemList
import com.yehorsk.platea.orders.presentation.create_order.components.TotalPrice
import com.yehorsk.platea.orders.presentation.order_details.components.DeliveryDetails
import com.yehorsk.platea.orders.presentation.order_details.components.OrderStatus
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun OrderDetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: OrderDetailsViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    onOpenItemDetails: (Int) -> Unit,
    id: Int
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrderDetailsScreen(
        modifier = modifier,
        uiState = uiState,
        onGoToOrders = onGoBack,
        onAction = viewModel::onAction,
        onOpenItemDetails = onOpenItemDetails
    )

}

@Composable
fun OrderDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: OrderDetailsUiState,
    onGoToOrders: () -> Unit,
    onOpenItemDetails: (Int) -> Unit,
    onAction: (OrderDetailsAction) -> Unit,
){
    if(uiState.isLoading){
        LoadingPart()
    }else if(uiState.currentOrder != null){

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavBar(
                onGoBack = onGoToOrders,
                title = R.string.go_back
            )
            OrderStatus(
                status = uiState.currentOrder.status,
                date = uiState.currentOrder.createdAt,
                code = uiState.currentOrder.code,
                startTime = uiState.currentOrder.startTime,
                endTime = uiState.currentOrder.endTime,
                preparationDate = uiState.currentOrder.date ?: ""
            )
            HorizontalDivider()
            OrderDetailsItemList(
                order = uiState.currentOrder,
                onOpenItemDetails = { id ->
                    onOpenItemDetails(id)
                }
            )
            HorizontalDivider()
            TotalPrice(
                price = formattedPrice(uiState.currentOrder.price)
            )
            HorizontalDivider()
            if(uiState.currentOrder.orderType==1){
                DeliveryDetails(
                    address = uiState.currentOrder.address.toString(),
                    instructions = uiState.currentOrder.instructions.toString(),
                    phone = uiState.currentOrder.phone.toString()
                )
            }
            if(uiState.isNetwork){
                if(uiState.userRole == "user"){
                    Column(
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                    ) {
                        if(uiState.currentOrder.status == "Pending"){
                            ActionButton(
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 5.dp,
                                        bottom = 20.dp
                                    ),
                                onAction = { onAction(OrderDetailsAction.UserCancelOrder((uiState.currentOrder.id))) },
                                text = R.string.cancel_order,
                                enabled = uiState.currentOrder.status == "Pending"
                            )
                        }
                    }
                }else{
                    Column(
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                    ) {
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetConfirmedStatus((uiState.currentOrder.id))) },
                            text = R.string.confirm_order,
                            enabled = uiState.currentOrder.status != "Confirmed"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetPreparingStatus((uiState.currentOrder.id))) },
                            text = R.string.prepare_order,
                            enabled = uiState.currentOrder.status != "Preparing"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetCompletedStatus((uiState.currentOrder.id))) },
                            text = R.string.complete_order,
                            enabled = uiState.currentOrder.status != "Completed"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetReadyForPickupStatus((uiState.currentOrder.id))) },
                            text = R.string.ready_order,
                            enabled = uiState.currentOrder.status != "Ready for Pickup"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetCancelledStatus((uiState.currentOrder.id))) },
                            text = R.string.cancel_order,
                            enabled = uiState.currentOrder.status != "Cancelled"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderDetailsScreenPreview(){
    MobileTheme {
        OrderDetailsScreenRoot(
            onGoBack = {},
            id = 1,
            onOpenItemDetails = {}
        )
    }
}