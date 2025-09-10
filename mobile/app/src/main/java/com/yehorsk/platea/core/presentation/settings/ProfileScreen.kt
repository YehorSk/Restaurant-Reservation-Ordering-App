package com.yehorsk.platea.core.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.AutoResizedText
import com.yehorsk.platea.core.presentation.components.ConfirmDialog
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.PhoneInput
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    onGoBack: () -> Unit,
    onDeleteAccount: () -> Unit
 ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onDeleteAccount()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.go_back
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
                bottom = 15.dp
            ),
            text = stringResource(R.string.profile),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
        )
        ProfileScreenForm(
            name = uiState.name,
            address = uiState.address,
            phone = uiState.phone,
            code = uiState.code,
            isPhoneValid = uiState.isPhoneValid,
            onAction = viewModel::onAction
        )
    }
    if(uiState.showDeleteAccountDialog){
        ConfirmDialog(
            dialogTitle = stringResource(id = R.string.delete_account_dialog_title),
            dialogText = stringResource(id = R.string.delete_account_dialog_text),
            onDismissRequest = {
                viewModel.onAction(SettingsAction.HideDeleteDialog)
            },
            onConfirmation = {
                viewModel.onAction(SettingsAction.DeleteAccount)
            },
        )
    }
}

@Composable
fun ProfileScreenForm(
    modifier: Modifier = Modifier,
    name: String,
    address: String,
    phone: String,
    code: String,
    isPhoneValid: Boolean,
    onAction: (SettingsAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var tempName by remember { mutableStateOf(name) }
    var tempAddress by remember { mutableStateOf(address) }
    var tempPhone by remember { mutableStateOf(phone) }
    var tempCode by remember { mutableStateOf(code) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = tempName,
            onValueChange = { tempName = it },
            label = { Text(text = stringResource(R.string.user_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        PhoneInput(
            modifier = Modifier.fillMaxWidth(),
            phone = tempPhone,
            code = tempCode,
            onPhoneValidated = { onAction(SettingsAction.ValidatePhoneNumber(it)) },
            onPhoneChanged = { tempPhone = it },
            onCodeChanged = { tempCode = it },
            showText = false
        )
        OutlinedTextField(
            value = tempAddress,
            onValueChange = { tempAddress = it },
            label = { Text(text = stringResource(R.string.user_address)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = {
                keyboardController?.hide()
                onAction(SettingsAction.UpdateProfile(tempName, tempAddress, tempPhone, tempCode))
                      },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            enabled = tempName.isNotEmpty() && tempAddress.isNotEmpty() && isPhoneValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            AutoResizedText(
                text = stringResource(R.string.update_profile),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        TextButton(
            onClick = { onAction(SettingsAction.ShowDeleteDialog) },
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
        ) {
            AutoResizedText(
                text = stringResource(R.string.delete_profile),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                maxLines = 1
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ProfileScreenFormPreview(){
    MobileTheme {
        ProfileScreenForm(
            name = "Test",
            address = "Test",
            phone = "Test",
            code = "421",
            isPhoneValid = true,
            onAction = {it ->}
        )
    }
}
