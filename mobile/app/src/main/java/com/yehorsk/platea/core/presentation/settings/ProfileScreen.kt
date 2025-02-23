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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.AutoResizedText
import com.yehorsk.platea.core.presentation.components.ConfirmDialog
import com.yehorsk.platea.core.presentation.components.PhoneInput
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.orders.presentation.components.NavBar
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
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
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
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        ProfileScreenForm(
            name = uiState.name,
            address = uiState.address,
            phone = uiState.phone,
            code = uiState.code,
            onUpdateProfile = { viewModel.updateProfile() },
            onOpenDialog = { viewModel.showDeleteDialog() },
            checkPhone = { viewModel.validatePhoneNumber(it) },
            isPhoneValid = uiState.isPhoneValid,
            onUpdatePhone = { viewModel.updatePhone(it) },
            onUpdateCode = { viewModel.updateCode(it) },
            onUpdateName = { viewModel.updateName(it) },
            onUpdateAddress = { viewModel.updateAddress(it) },
        )
    }
    if(uiState.showDeleteAccountDialog){
        ConfirmDialog(
            dialogTitle = stringResource(id = R.string.delete_account_dialog_title),
            dialogText = stringResource(id = R.string.delete_account_dialog_text),
            onDismissRequest = {
                viewModel.hideDeleteDialog()
            },
            onConfirmation = {
                viewModel.deleteAccount()
            },
        )
    }
}

@Composable
fun ProfileScreenForm(
    name: String,
    address: String,
    phone: String,
    code: String,
    isPhoneValid: Boolean,
    onUpdateProfile: () -> Unit,
    onOpenDialog: () -> Unit,
    checkPhone: (String) -> Unit,
    onUpdatePhone: (String) -> Unit,
    onUpdateCode: (String) -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateAddress: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { onUpdateName(it) },
            label = { Text(text = stringResource(R.string.user_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        PhoneInput(
            modifier = Modifier.fillMaxWidth(),
            phone = phone,
            code = code,
            onPhoneValidated = { checkPhone(it) },
            onPhoneChanged = { onUpdatePhone(it) },
            onCodeChanged = { onUpdateCode(it) },
            showText = false
        )
        OutlinedTextField(
            value = address,
            onValueChange = { onUpdateAddress(it) },
            label = { Text(text = stringResource(R.string.user_address)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { onUpdateProfile() },
            enabled = name.isNotEmpty() && address.isNotEmpty() && isPhoneValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.update_profile),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        TextButton(
            onClick = { onOpenDialog() },
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
            onUpdateProfile = { },
            onOpenDialog = {},
            checkPhone = { it -> true },
            isPhoneValid = false,
            onUpdatePhone = {},
            onUpdateCode = {},
            onUpdateName = {},
            onUpdateAddress = {},
        )
    }
}
