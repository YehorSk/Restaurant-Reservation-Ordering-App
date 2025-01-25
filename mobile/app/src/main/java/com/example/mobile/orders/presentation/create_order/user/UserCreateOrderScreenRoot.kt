package com.example.mobile.orders.presentation.create_order.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.LoadingPart
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.data.remote.dto.TableDto
import com.example.mobile.orders.presentation.OrderUiState
import com.example.mobile.orders.presentation.components.DeliveryMap
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.components.OrderAddMore
import com.example.mobile.orders.presentation.components.OrderAddress
import com.example.mobile.orders.presentation.components.OrderItemList
import com.example.mobile.orders.presentation.components.OrderOptions
import com.example.mobile.orders.presentation.components.OrderSpecialRequest
import com.example.mobile.orders.presentation.components.PickupMap
import com.example.mobile.orders.presentation.components.SelectTable
import com.example.mobile.orders.presentation.components.TotalPrice
import com.example.mobile.orders.presentation.create_order.CreateOrderAction
import com.example.mobile.orders.presentation.create_order.CreateOrderViewModel
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun UserCreateOrderScreenRoot(
    viewModel: CreateOrderViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToOrders: () -> Unit,
    onGoToMakeReservation: () -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val context = LocalContext.current
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUserOrderItems()
        viewModel.getTables()
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> onGoToOrders()
        }
    }

    UserCreateOrderScreen(
        modifier = modifier,
        uiState = uiState,
        isConnected = isConnected,
        onGoToCart = onGoToCart,
        onGoToMenu = onGoToMenu,
        onGoToMakeReservation = onGoToMakeReservation,
        validateForm = viewModel.validateForm(),
        onAction = viewModel::onAction,
        userRole = userRole.toString(),
        onTableNumberUpdate = { viewModel.updateTableNumber(it) }
    )

}

@Composable
fun UserCreateOrderScreen(
    modifier: Modifier = Modifier,
    uiState: OrderUiState,
    isConnected: Boolean,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToMakeReservation: () -> Unit,
    onTableNumberUpdate: (TableDto) -> Unit,
    validateForm: Boolean,
    onAction: (CreateOrderAction) -> Unit,
    userRole: String
){

    var isMapLoaded by remember { mutableStateOf(false) }

    if(uiState.orderItems != null){
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {

            NavBar(
                onGoBack = onGoToCart,
                title = R.string.go_back
            )
            if (isConnected){
                OrderItemList(items = uiState.orderItems)
                HorizontalDivider()
            }
            OrderAddMore(
                onGoToCart = onGoToCart,
                onGoToMenu = onGoToMenu
            )
            HorizontalDivider()
            OrderSpecialRequest(
                request = uiState.orderForm.specialRequest,
                onRequestChange = {request -> onAction(CreateOrderAction.UpdateRequest(request)) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            if(userRole == "user"){
                OrderOptions(
                    selected = uiState.orderForm.orderType,
                    onSelectedChange = { type,text -> onAction(CreateOrderAction.UpdateOrderType(type,text)) }
                )
            }else{
                if(uiState.tables !=null){
                    SelectTable(
                        tables = uiState.tables!!,
                        selectedTable = uiState.orderForm.selectedTable,
                        onTableChanged = { onTableNumberUpdate(it) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(userRole == "user") {
                if (uiState.orderForm.orderType == 0 && isConnected) {
                    PickupMap(
                        onMapLoaded = {
                            isMapLoaded = true
                        }
                    )
                }
                if (uiState.orderForm.orderType == 1 && isConnected) {
                    if (!uiState.places.isNullOrEmpty()) {
                        DeliveryMap()
                    }
                    OrderAddress(
                        address = uiState.orderForm.address,
                        instructions = uiState.orderForm.instructions,
                        places = uiState.places,
                        onAddressChange = { address ->
                            onAction(CreateOrderAction.UpdateAddress(address))
//                                   onAction(CreateOrderAction.UpdatePlace(address))
                        },
                        onInstructionsChange = { instructions ->
                            onAction(
                                CreateOrderAction.UpdateInstructions(
                                    instructions
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            if (isConnected){
                val checkout = uiState.orderItems.sumOf {
                    it.pivot.price
                }
                TotalPrice(
                    price = formattedPrice(checkout)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .fillMaxWidth(),
                    enabled = validateForm,
                    onClick = {
                        if(userRole == "user"){
                            if (uiState.orderForm.orderType==2){
                                onGoToMakeReservation()
                            }else{
                                onAction(CreateOrderAction.MakeOrder)
                            }
                        }else{
                            onAction(CreateOrderAction.MakeWaiterOrder)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = when (uiState.orderForm.orderType) {
                                0 -> R.string.order_button_pickup
                                1 -> R.string.order_button_delivery
                                2 -> R.string.order_button_reservation
                                else -> R.string.order_button_pickup
                            }
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }else{
        LoadingPart()
    }
}


@Preview
@Composable
fun CreateOrderScreenPreview(){
    MobileTheme {
        UserCreateOrderScreenRoot(
            onGoToMenu = {},
            onGoToCart = {},
            onGoToOrders = {},
            onGoToMakeReservation = {}
        )
    }
}