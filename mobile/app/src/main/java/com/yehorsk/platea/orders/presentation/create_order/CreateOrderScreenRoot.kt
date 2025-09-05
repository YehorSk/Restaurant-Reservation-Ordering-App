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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.presentation.create_order.components.ChooseTimeModal
import com.yehorsk.platea.orders.presentation.create_order.components.DeliveryDetails
import com.yehorsk.platea.orders.presentation.create_order.components.OrderAddMore
import com.yehorsk.platea.orders.presentation.create_order.components.OrderItemList
import com.yehorsk.platea.orders.presentation.create_order.components.OrderOptions
import com.yehorsk.platea.orders.presentation.create_order.components.OrderSpecialRequest
import com.yehorsk.platea.orders.presentation.create_order.components.PickupDetails
import com.yehorsk.platea.orders.presentation.create_order.components.SelectTable
import com.yehorsk.platea.orders.presentation.create_order.components.TotalPrice
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
    val cartItemsUiState by viewModel.cartItemsUiState.collectAsStateWithLifecycle()
    val info by viewModel.restaurantInfoUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUserOrderItems()
        viewModel.getTables()
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoToCart()
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
            is SideEffect.LanguageChanged -> {}
        }
    }

    CreateOrderScreen(
        modifier = modifier,
        uiState = uiState,
        items = cartItemsUiState,
        onGoToCart = onGoToCart,
        onGoToMenu = onGoToMenu,
        onGoToMakeReservation = { onGoToMakeReservation(uiState.orderForm) },
        validateForm = viewModel.validateForm(),
        onAction = viewModel::onAction,
        onTableNumberUpdate = { viewModel.updateTableNumber(it) },
        schedule = info
    )
}

@Composable
fun CreateOrderScreen(
    modifier: Modifier = Modifier,
    uiState: CreateOrderUiState,
    items: List<CartItem>,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToMakeReservation: () -> Unit,
    onTableNumberUpdate: (TableDto) -> Unit,
    validateForm: Boolean,
    onAction: (CreateOrderAction) -> Unit,
    schedule: RestaurantInfoEntity?
){

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
            if (uiState.isNetwork){
                OrderItemList(items = items)
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
            if(uiState.userRole == "user" && schedule != null){
                OrderOptions(
                    selected = uiState.orderForm.orderType,
                    onSelectedChange = { type,text ->
                        onAction(CreateOrderAction.UpdateOrderType(type,text))
                        if(type != 2){
                            onAction(CreateOrderAction.OpenBottomSheet)
                        }
                    },
                    selectedTime = "${uiState.orderForm.startTime} - ${uiState.orderForm.endTime}"
                )
            }else{
                onAction(CreateOrderAction.UpdateOrderType(3, "waiter"))
                if(uiState.tables !=null){
                    SelectTable(
                        tables = uiState.tables,
                        selectedTable = uiState.orderForm.selectedTable,
                        onTableChanged = { onTableNumberUpdate(it) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(uiState.userRole == "user") {
                if (uiState.orderForm.orderType == 0 && uiState.isNetwork) {
                    PickupDetails()
                }
                if (uiState.orderForm.orderType == 1 && uiState.isNetwork) {
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
            if (uiState.isNetwork){
                val checkout = items.sumOf {
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    onClick = {
                        if(uiState.userRole == "user"){
                            if (uiState.orderForm.orderType==2){
                                onGoToMakeReservation()
                            }else{
                                if(uiState.orderForm.startTime.isNotEmpty() && uiState.orderForm.endTime.isNotEmpty()){
                                    onAction(CreateOrderAction.MakeOrder)
                                }else{
                                    onAction(CreateOrderAction.OpenBottomSheet)
                                }
                            }
                        }else{
                            onAction(CreateOrderAction.MakeWaiterOrder)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = when (uiState.userRole) {
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
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        if(uiState.showBottomSheet && schedule != null){
            ChooseTimeModal(
                onDismiss = { onAction(CreateOrderAction.CloseBottomSheet) },
                onTimeSelect = { start, end ->
                                    onAction(CreateOrderAction.UpdateTime(start, end))
                                    onAction(CreateOrderAction.CloseBottomSheet)
                               },
                selectedTime = "${uiState.orderForm.startTime} - ${uiState.orderForm.endTime}",
                selectedDate = uiState.orderForm.selectedDate,
                onDateSelect = { date -> onAction(CreateOrderAction.UpdateDate(date))},
                schedule = schedule.openingHours
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