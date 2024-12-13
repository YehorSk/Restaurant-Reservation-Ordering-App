package com.example.mobile.orders.presentation.create_reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.example.mobile.orders.presentation.create_reservation.components.CustomCalendar

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
        CustomCalendar()
    }
}