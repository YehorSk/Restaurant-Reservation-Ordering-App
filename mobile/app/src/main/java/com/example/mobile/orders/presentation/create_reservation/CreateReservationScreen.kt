package com.example.mobile.orders.presentation.create_reservation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.create_reservation.components.PartySize
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.create_reservation.components.CalendarRoot
import com.example.mobile.orders.presentation.create_reservation.components.TimeRoot

@Composable
fun CreateReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateReservationViewModel = hiltViewModel(),
    goBack: ()->Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected = viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(
            onGoBack = goBack,
            title = R.string.go_back
        )
        PartySize(
            onPartySizeChanged = { size -> viewModel.updatePartySize(size) },
            partySize = uiState.orderForm.partySize
        )
        CalendarRoot(
            onUpdateSelectedDate = { date -> viewModel.updateReservationDate(date) }
        )
        TimeRoot(date = uiState.orderForm.reservationDate) { }
    }
}