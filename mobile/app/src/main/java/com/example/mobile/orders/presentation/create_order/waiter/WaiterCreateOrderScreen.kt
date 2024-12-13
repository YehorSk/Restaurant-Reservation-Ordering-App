package com.example.mobile.orders.presentation.create_order.waiter

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.components.OrderAddMore
import com.example.mobile.orders.presentation.components.OrderAddress
import com.example.mobile.orders.presentation.components.OrderItemList
import com.example.mobile.orders.presentation.components.OrderMap
import com.example.mobile.orders.presentation.components.OrderOptions
import com.example.mobile.orders.presentation.components.OrderSpecialRequest
import com.example.mobile.orders.presentation.components.SelectTable
import com.example.mobile.orders.presentation.components.TotalPrice
import com.example.mobile.orders.presentation.create_order.CreateOrderViewModel

@Composable
fun WaiterCreateOrderScreen(
    viewModel: CreateOrderViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
    onGoToOrders: () -> Unit,
){

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected = viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.getUserOrderItems()
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            SideEffect.NavigateToNextScreen -> onGoToOrders()
        }
    }

    if(uiState.value.items != null){
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
            if (isConnected.value && uiState.value.items != null){
                OrderItemList(items = uiState.value.items!!)
                HorizontalDivider()
            }
            OrderAddMore(
                onGoToCart = onGoToCart,
                onGoToMenu = onGoToMenu
            )
            HorizontalDivider()
            OrderSpecialRequest(
                request = uiState.value.orderForm.specialRequest,
                onRequestChange = {request -> viewModel.updateRequest(request)}
            )
            Spacer(modifier = Modifier.height(10.dp))
//            OrderOptions(
//                selected = uiState.value.orderForm.orderType,
//                onSelectedChange = { type,text -> viewModel.updateOrderType(type,text) }
//            )
            val tables = listOf<Int>(1,2,3,4,5,6,7,8,9,10)
            SelectTable(
                tables = tables,
                selectedTable = uiState.value.orderForm.tableNumber,
                onTableNumberChanged = { viewModel.updateTableNumber(it) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (uiState.value.orderForm.orderType == 1 && isConnected.value){
                OrderMap()
                OrderAddress(
                    address = uiState.value.orderForm.address,
                    instructions = uiState.value.orderForm.instructions,
                    onAddressChange = {address -> viewModel.updateAddress(address)},
                    onInstructionsChange = {instructions -> viewModel.updateInstructions(instructions)}
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (isConnected.value  && uiState.value.items != null){
                val checkout = uiState.value.items!!.sumOf {
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
                    enabled = viewModel.validateForm(),
                    onClick = {
                        viewModel.makeOrder()
                    }
                ) {
                    Text(
                        text = "Place Order",
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