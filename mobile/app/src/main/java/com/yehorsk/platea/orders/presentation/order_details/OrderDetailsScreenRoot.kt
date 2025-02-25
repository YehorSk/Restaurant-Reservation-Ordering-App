package com.yehorsk.platea.orders.presentation.order_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.presentation.components.ActionButton
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import com.yehorsk.platea.orders.presentation.OrderUiState
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.orders.presentation.components.OrderDetailsItemList
import com.yehorsk.platea.orders.presentation.components.TotalPrice
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
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val ordersUiState by viewModel.ordersUiState.collectAsStateWithLifecycle()
    val role by viewModel.userRole.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoBack()
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getOrderDetails(id.toString())
    }

    OrderDetailsScreen(
        modifier = modifier,
        ordersUiState = ordersUiState,
        uiState = uiState,
        onGoToOrders = onGoBack,
        id = id,
        onAction = viewModel::onAction,
        isConnected = isConnected,
        role = role.toString(),
        onOpenItemDetails = onOpenItemDetails
    )

}

@Composable
fun OrderDetailsScreen(
    modifier: Modifier = Modifier,
    ordersUiState: List<OrderWithOrderItems>,
    uiState: OrderUiState,
    onGoToOrders: () -> Unit,
    onOpenItemDetails: (Int) -> Unit,
    id: Int,
    isConnected: Boolean,
    onAction: (OrderDetailsAction) -> Unit,
    role: String
){
    val data = ordersUiState.find { it.order.id == id.toString() }
    if(uiState.isLoading){
        LoadingPart()
    }else if(data!=null){

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
                status = data.order.status,
                date = data.order.createdAt,
                code = data.order.code
            )
            OrderDetailsItemList(
                order = data,
                onOpenItemDetails = { id ->
                    onOpenItemDetails(id)
                }
            )
            HorizontalDivider()
            TotalPrice(
                price = formattedPrice(data.order.price)
            )
            HorizontalDivider()
            if(data.order.orderType==1){
                DeliveryDetails(
                    address = data.order.address.toString(),
                    instructions = data.order.instructions.toString(),
                    phone = data.order.phone.toString()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(isConnected){
                if(role == "user"){
                    Column(
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                    ) {
                        if(data.order.status == "Pending"){
                            ActionButton(
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 5.dp,
                                        bottom = 20.dp
                                    ),
                                onAction = { onAction(OrderDetailsAction.UserCancelOrder((data.order.id))) },
                                text = R.string.cancel_order,
                                enabled = data.order.status == "Pending"
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
                            onAction = { onAction(OrderDetailsAction.SetConfirmedStatus((data.order.id))) },
                            text = R.string.confirm_order,
                            enabled = data.order.status != "Confirmed"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetPreparingStatus((data.order.id))) },
                            text = R.string.prepare_order,
                            enabled = data.order.status != "Preparing"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetCompletedStatus((data.order.id))) },
                            text = R.string.complete_order,
                            enabled = data.order.status != "Completed"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetReadyForPickupStatus((data.order.id))) },
                            text = R.string.ready_order,
                            enabled = data.order.status != "Ready for Pickup"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 5.dp,
                                    bottom = 20.dp
                                ),
                            onAction = { onAction(OrderDetailsAction.SetCancelledStatus((data.order.id))) },
                            text = R.string.cancel_order,
                            enabled = data.order.status != "Cancelled"
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