package com.yehorsk.platea.notifications.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.utils.EventConsumer
import com.yehorsk.platea.notifications.presentation.NotificationViewModel
import com.yehorsk.platea.notifications.presentation.components.NotificationItem
import com.yehorsk.platea.orders.presentation.components.NavBar

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    showGoBack: Boolean = false
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationUiState by viewModel.notificationUiState.collectAsStateWithLifecycle()

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        NavBar(
            onGoBack = onGoBack,
            title = R.string.notifications,
            showGoBack = showGoBack
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = notificationUiState,
                key = { it.id }
            ){ item ->
                NotificationItem(
                    notification = item,
                    onClick = { menuItem ->
                    }
                )
                HorizontalDivider()
            }

        }
    }
}