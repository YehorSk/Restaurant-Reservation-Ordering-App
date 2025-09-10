package com.yehorsk.platea.core.presentation.settings

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
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.presentation.settings.components.ProfileListHeader
import com.yehorsk.platea.core.presentation.settings.components.SettingsListItem
import com.yehorsk.platea.core.utils.SideEffect

@Composable
fun ChangeThemeScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onGoBack: () -> Unit
){

    val userTheme by viewModel.userTheme.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoBack()
        }
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.go_back
        )
        ChangeThemeScreenRoot(
            onAction = viewModel::onAction,
            userTheme = userTheme == true
        )
    }
}

@Composable
fun ChangeThemeScreenRoot(
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit,
    userTheme: Boolean
){
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item{
            ProfileListHeader(
                text = stringResource(R.string.theme)
            )
        }
        item{
            SettingsListItem(
                text = R.string.theme_light,
                onClick = {
                    onAction(SettingsAction.UpdateTheme(false))
                },
                isActive = userTheme == false
            )
        }
        item{
            SettingsListItem(
                text = R.string.theme_dark,
                onClick = {
                    onAction(SettingsAction.UpdateTheme(true))
                },
                isActive = userTheme == true
            )
        }
    }
}