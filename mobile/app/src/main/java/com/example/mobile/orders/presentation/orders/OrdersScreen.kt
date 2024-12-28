package com.example.mobile.orders.presentation.orders

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.OrderFilter
import com.example.mobile.orders.presentation.orders.components.OrdersList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    modifier:Modifier = Modifier,
    onGoToOrderDetails: (Int) -> Unit,
    viewModel: OrdersViewModel = hiltViewModel()
){

    val context = LocalContext.current
    val orders by viewModel.orderItemsUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterOption by viewModel.filterOption.collectAsStateWithLifecycle()

    var expanded by remember { mutableStateOf(false) }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.getUserOrders() }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(text = "Filter: ${filterOption.name}")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    OrderFilter.entries.forEach { filter ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.updateFilter(filter)
                                expanded = false
                            },
                            text = {
                                Text(text = filter.name)
                            }
                        )
                    }
                }
            }
            OrdersList(
                orders = orders,
                onGoToOrderDetails = { onGoToOrderDetails(it) }
            )
        }
    }

}