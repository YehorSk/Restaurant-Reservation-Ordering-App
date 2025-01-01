package com.example.mobile.core.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.core.presentation.settings.components.ProfileListItem
import timber.log.Timber
import com.example.mobile.R
import com.example.mobile.core.presentation.settings.components.ProfileListHeader
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
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
                ProfileListHeader(
                    text = R.string.settings
                )
            }
            item{
                ProfileListItem(
                    text = R.string.profile,
                    onClick = {
                        onNavigate(ProfileDestination.Profile)
                    }
                )
            }
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

@PreviewLightDark
@Composable
fun ProfileScreenPreview(){
    MobileTheme {
        SettingsScreen(
            onNavigate = {}
        )
    }
}