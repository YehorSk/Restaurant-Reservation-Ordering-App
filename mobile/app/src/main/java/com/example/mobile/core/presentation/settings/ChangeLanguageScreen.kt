package com.example.mobile.core.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.presentation.settings.components.ProfileListHeader
import com.example.mobile.core.presentation.settings.components.SettingsListItem
import com.example.mobile.orders.presentation.components.NavBar

@Composable
fun ChangeLanguageScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onGoBack: () -> Unit
){

    val userLang by viewModel.userLang.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.go_back
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item{
                SettingsListItem(
                    text = R.string.english,
                    onClick = {
                        viewModel.updateLanguage("en")
                    },
                    isActive = userLang == "en"
                )
            }
            item{
                SettingsListItem(
                    text = R.string.slovak,
                    onClick = {
                        viewModel.updateLanguage("sk")
                    },
                    isActive = userLang == "sk"
                )
            }
            item{
                SettingsListItem(
                    text = R.string.ukrainian,
                    onClick = {
                        viewModel.updateLanguage("uk")
                    },
                    isActive = userLang == "uk"
                )
            }
        }
    }
}