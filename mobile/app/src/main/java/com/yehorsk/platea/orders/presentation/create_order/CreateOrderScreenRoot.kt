package com.yehorsk.platea.orders.presentation.create_order

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
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.orders.presentation.OrderUiState
import com.yehorsk.platea.orders.presentation.components.ChooseTimeModal
import com.yehorsk.platea.orders.presentation.components.DeliveryDetails
import com.yehorsk.platea.orders.presentation.components.OrderAddMore
import com.yehorsk.platea.orders.presentation.components.OrderItemList
import com.yehorsk.platea.orders.presentation.components.OrderOptions
import com.yehorsk.platea.orders.presentation.components.OrderSpecialRequest
import com.yehorsk.platea.orders.presentation.components.PickupDetails
import com.yehorsk.platea.orders.presentation.components.SelectTable
import com.yehorsk.platea.orders.presentation.components.TotalPrice
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun CreateOrderScreenRoot(
    viewModel: CreateOrderViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToMakeReservation: (OrderForm) -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val userAddress by viewModel.userAddress.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUserOrderItems()
        viewModel.getTables()
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoToCart()
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
        }
    }

    CreateOrderScreen(
        modifier = modifier,
        uiState = uiState,
        isConnected = isConnected,
        onGoToCart = onGoToCart,
        onGoToMenu = onGoToMenu,
        onGoToMakeReservation = { onGoToMakeReservation(uiState.orderForm) },
        validateForm = viewModel.validateForm(),
        onAction = viewModel::onAction,
        userRole = userRole.toString(),
        userAddress = userAddress.toString(),
        onTableNumberUpdate = { viewModel.updateTableNumber(it) }
    )
}

@Composable
fun CreateOrderScreen(
    modifier: Modifier = Modifier,
    uiState: OrderUiState,
    isConnected: Boolean,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToMakeReservation: () -> Unit,
    onTableNumberUpdate: (TableDto) -> Unit,
    validateForm: Boolean,
    onAction: (CreateOrderAction) -> Unit,
    userRole: String,
    userAddress: String,
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
                    onSelectedChange = { type,text ->
                        if(type != 3){
                            onAction(CreateOrderAction.UpdateOrderType(type,text))
                        }else{
                            onAction(CreateOrderAction.OpenBottomSheet)
                        }
                    },
                    selectedTime = "${uiState.orderForm.startTime} - ${uiState.orderForm.endTime}"
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
                    PickupDetails()
                }
                if (uiState.orderForm.orderType == 1 && isConnected) {
                    DeliveryDetails(
                        address = uiState.orderForm.address,
                        instructions = uiState.orderForm.instructions,
                        onAddressChange = { address ->
                            onAction(CreateOrderAction.UpdateAddress(address))
                        },
                        onInstructionsChange = { instructions ->
                            onAction(
                                CreateOrderAction.UpdateInstructions(
                                    instructions
                                )
                            )
                        },
                        onPhoneValidate = {
                            onAction(
                                CreateOrderAction.ValidatePhone(it)
                            )
                        },
                        onPhoneChanged = {  phone ->
                            onAction(
                                CreateOrderAction.UpdatePhone(
                                    phone
                                )
                            ) },
                        code = uiState.countryCode,
                        phone = uiState.phone
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
                            id = when (userRole) {
                                "waiter" -> R.string.order_button_place
                                "user" -> when (uiState.orderForm.orderType) {
                                    0 -> R.string.order_button_pickup
                                    1 -> R.string.order_button_delivery
                                    2 -> R.string.order_button_reservation
                                    else -> R.string.order_button_pickup
                                }
                                else -> R.string.order_button_pickup
                            }
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        if(uiState.showBottomSheet){
            ChooseTimeModal(
                onDismiss = { onAction(CreateOrderAction.CloseBottomSheet) },
                onOrderTypeSelect = { type,text -> onAction(CreateOrderAction.UpdateOrderType(type,text)) },
                onTimeSelect = { start, end ->
                                    onAction(CreateOrderAction.UpdateTime(start, end))
                                    onAction(CreateOrderAction.CloseBottomSheet)
                                    onAction(CreateOrderAction.UpdateOrderType(uiState.orderForm.orderType,uiState.orderForm.orderText))
                               },
                selectedTime = "${uiState.orderForm.startTime} - ${uiState.orderForm.endTime}",
                selected = uiState.orderForm.orderType
            )
        }
    }else{
        LoadingPart()
    }
}


@Preview
@Composable
fun CreateOrderScreenPreview(){
    MobileTheme {
        CreateOrderScreenRoot(
            onGoToMenu = {},
            onGoToCart = {},
            onGoToMakeReservation = {}
        )
    }
}