package com.example.mobile.core.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import timber.log.Timber

@Composable
fun SettingsScreen(
    modifier: Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onSuccessLoggedOut: () -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
        Button(
            onClick = settingsViewModel::logout
        ) {
            Text(text = "LogOut")
        }
        LaunchedEffect(uiState.isLoggedOut) {
            Timber.tag("LaunchedEffect").v("UI State Is Logged Out: ${uiState.isLoggedOut}")
            if(uiState.isLoggedOut){
                onSuccessLoggedOut()
            }
        }
    }
}