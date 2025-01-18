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
import com.example.mobile.core.presentation.settings.components.ThemeListItem
import com.example.mobile.orders.presentation.components.NavBar

@Composable
fun ChangeThemeScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onGoBack: () -> Unit
){

    val userTheme by viewModel.userTheme.collectAsStateWithLifecycle()
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
                ProfileListHeader(
                    text = stringResource(R.string.theme)
                )
            }
            item{
                ThemeListItem(
                    text = R.string.theme_light,
                    onClick = {
                        viewModel.updateTheme(false)
                    },
                    isActive = userTheme == false
                )
            }
            item{
                ThemeListItem(
                    text = R.string.theme_dark,
                    onClick = {
                        viewModel.updateTheme(true)
                    },
                    isActive = userTheme == true
                )
            }
        }
    }
}