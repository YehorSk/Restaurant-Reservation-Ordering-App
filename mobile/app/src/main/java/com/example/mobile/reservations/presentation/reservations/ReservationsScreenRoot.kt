package com.example.mobile.reservations.presentation.reservations

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.reservations.presentation.reservations.components.ReservationsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ReservationScreenViewModel = hiltViewModel(),
    onGoToReservationDetails: (Int) -> Unit
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reservations by viewModel.reservationItemUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }
    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.getReservations() }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReservationsList(
                items = reservations,
                onGoToReservationDetails = { onGoToReservationDetails(it) }
            )
        }
    }
}