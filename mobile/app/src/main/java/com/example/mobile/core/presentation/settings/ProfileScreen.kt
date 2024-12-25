package com.example.mobile.core.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.core.presentation.settings.components.ProfileListItem
import timber.log.Timber
import com.example.mobile.R

@Composable
fun ProfileScreen(
    modifier: Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onNavigate: (ProfileDestination) -> Unit
){
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
    ) {
        val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item{
                ProfileListItem(
                    text = R.string.favorites,
                    onClick = {
                        onNavigate(ProfileDestination.Favorites)
                    }
                )
            }
            item{
                ProfileListItem(
                    text = R.string.logout,
                    onClick = {
                        settingsViewModel.logout()
                    }
                )
            }
        }
        LaunchedEffect(uiState.isLoggedOut) {
            Timber.tag("LaunchedEffect").v("UI State Is Logged Out: ${uiState.isLoggedOut}")
            if(uiState.isLoggedOut){
                onNavigate(ProfileDestination.Logout)
            }
        }
    }
}