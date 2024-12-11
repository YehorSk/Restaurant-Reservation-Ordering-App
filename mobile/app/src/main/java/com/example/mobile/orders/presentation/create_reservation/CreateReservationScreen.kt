package com.example.mobile.orders.presentation.create_reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile.R
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.create_reservation.components.PartySize

@Composable
fun CreateReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateReservationViewModel = hiltViewModel(),
    goBack: ()->Unit
){
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
        PartySize()
    }
}