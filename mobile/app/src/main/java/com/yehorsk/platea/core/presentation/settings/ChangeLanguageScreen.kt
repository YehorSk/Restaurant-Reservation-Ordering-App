package com.yehorsk.platea.core.presentation.settings

import android.widget.Toast
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
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.presentation.settings.components.SettingsListItem
import com.yehorsk.platea.core.utils.toString
import com.yehorsk.platea.orders.presentation.components.NavBar

@Composable
fun ChangeLanguageScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onGoBack: () -> Unit
){

    val userLang by viewModel.userLang.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoBack()
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
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
            item{
                SettingsListItem(
                    text = R.string.german,
                    onClick = {
                        viewModel.updateLanguage("de")
                    },
                    isActive = userLang == "de"
                )
            }
        }
    }
}