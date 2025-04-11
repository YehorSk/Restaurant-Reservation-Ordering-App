package com.yehorsk.platea.core.presentation.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.presentation.settings.components.SettingsListItem
import com.yehorsk.platea.core.utils.SideEffect
import timber.log.Timber

@Composable
fun ChangeLanguageScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onGoBack: () -> Unit
){

    val userLang by viewModel.userLang.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
            is SideEffect.NavigateToNextScreen -> onGoBack()
            is SideEffect.LanguageChanged -> {}
        }
    }

    ChangeLanguageScreenRoot(
        modifier = modifier,
        onAction = viewModel::onAction,
        onGoBack = onGoBack,
        userLang = userLang.toString()
    )
}

@Composable
fun ChangeLanguageScreenRoot(
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit,
    userLang: String,
    onGoBack: () -> Unit
){
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
                        onAction(SettingsAction.UpdateLanguage("en"))
                    },
                    isActive = userLang == "en"
                )
            }
            item{
                SettingsListItem(
                    text = R.string.slovak,
                    onClick = {
                        onAction(SettingsAction.UpdateLanguage("sk"))
                    },
                    isActive = userLang == "sk"
                )
            }
            item{
                SettingsListItem(
                    text = R.string.ukrainian,
                    onClick = {
                        onAction(SettingsAction.UpdateLanguage("uk"))
                    },
                    isActive = userLang == "uk"
                )
            }
            item{
                SettingsListItem(
                    text = R.string.german,
                    onClick = {
                        onAction(SettingsAction.UpdateLanguage("de"))
                    },
                    isActive = userLang == "de"
                )
            }
        }
    }
}