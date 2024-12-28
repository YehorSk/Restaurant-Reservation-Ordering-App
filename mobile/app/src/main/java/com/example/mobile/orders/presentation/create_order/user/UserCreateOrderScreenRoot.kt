package com.example.mobile.orders.presentation.create_order.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.components.OrderAddMore
import com.example.mobile.orders.presentation.components.OrderItemList
import com.example.mobile.orders.presentation.components.OrderMap
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.orders.presentation.components.OrderOptions
import com.example.mobile.orders.presentation.components.TotalPrice
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.orders.presentation.components.OrderAddress
import com.example.mobile.orders.presentation.components.OrderSpecialRequest
import com.example.mobile.orders.presentation.create_order.CreateOrderAction
import com.example.mobile.orders.presentation.CreateOrderUiState
import com.example.mobile.orders.presentation.create_order.CreateOrderViewModel
import androidx.compose.runtime.getValue
import com.example.mobile.core.utils.toString

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

    LaunchedEffect(Unit) {
        viewModel.getUserOrderItems()
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
        onGoToOrders = onGoToOrders,
        onGoToMakeReservation = onGoToMakeReservation,
        validateForm = viewModel.validateForm(),
        onAction = viewModel::onAction
    )

}

@Composable
fun UserCreateOrderScreen(
    modifier: Modifier = Modifier,
    uiState: CreateOrderUiState,
    isConnected: Boolean,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToOrders: () -> Unit,
    onGoToMakeReservation: () -> Unit,
    validateForm: Boolean,
    onAction: (CreateOrderAction) -> Unit
){
    if(uiState.items != null){
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {

            NavBar(
                onGoBack = onGoToCart,
                title = R.string.complete_order_navbar_title
            )
            if (isConnected){
                OrderItemList(items = uiState.items)
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
            OrderOptions(
                selected = uiState.orderForm.orderType,
                onSelectedChange = { type,text -> onAction(CreateOrderAction.UpdateOrderType(type,text)) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (uiState.orderForm.orderType == 1 && isConnected){
                OrderMap()
                OrderAddress(
                    address = uiState.orderForm.address,
                    instructions = uiState.orderForm.instructions,
                    onAddressChange = {address -> onAction(CreateOrderAction.UpdateAddress(address)) },
                    onInstructionsChange = {instructions -> onAction(CreateOrderAction.UpdateInstructions(instructions)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (isConnected){
                val checkout = uiState.items.sumOf {
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
                        if (uiState.orderForm.orderType==2){
                            onGoToMakeReservation()
                        }else{
                            onAction(CreateOrderAction.MakeOrder)
                        }
                    }
                ) {
                    Text(
                        text = "Order ${uiState.orderForm.orderText}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }else{
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
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