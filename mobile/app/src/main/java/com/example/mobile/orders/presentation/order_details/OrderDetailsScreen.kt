package com.example.mobile.orders.presentation.order_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.Pivot
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.components.OrderItemList
import com.example.mobile.orders.presentation.components.TotalPrice
import com.example.mobile.orders.presentation.order_details.components.DeliveryDetails
import com.example.mobile.orders.presentation.order_details.components.OrderStatus
import com.example.mobile.ui.theme.MobileTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.presentation.components.OrderDetailsItemList
import com.example.mobile.orders.presentation.order_details.components.ActionButtons

@Composable
fun OrderDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderDetailsViewModel = hiltViewModel(),
    onGoToOrders: () -> Unit,
    id: Int
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ordersUiState by viewModel.ordersUiState.collectAsStateWithLifecycle()
    val isConnected = viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            SideEffect.NavigateToNextScreen -> onGoToOrders()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getOrderDetails(id.toString())
    }

    val data = ordersUiState.find { it.order.id == id.toString() }
    if(uiState.isLoading){
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator()
        }
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
            OrderDetailsItemList(order = data)
            HorizontalDivider()
            TotalPrice(
                price = formattedPrice(data.order.price)
            )
            HorizontalDivider()
            if(data.order.orderType==1){
                DeliveryDetails(
                    address = data.order.address.toString(),
                    instructions = data.order.instructions.toString()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ActionButtons(
                onRepeatOrder = { viewModel.repeatOrder(data.order.id) },
                onCancelOrder = { viewModel.cancelOrder(data.order.id) },
                allowCancel = data.order.status == "Pending"
            )
        }
    }

}

@Preview
@Composable
fun OrderDetailsScreenPreview(){
    MobileTheme {
        OrderDetailsScreen(
            onGoToOrders = {},
            id = 1
        )
    }
}