package com.yehorsk.platea.auth.presentation.forgot

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.toString

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ForgotBody(
                itemUiState = uiState,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                onValueChange = { viewModel.updateforgotUiState(it) },
                onUpdateClick = { viewModel.forgotPassword() }
            )
        }
    }
}

@Composable
fun ForgotBody(
    itemUiState: ForgotState,
    modifier: Modifier = Modifier,
    onValueChange: (ForgotFormState) -> Unit,
    onUpdateClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center
        )
        ForgotForm(
            forgotForm = itemUiState.form,
            onValueChange = onValueChange
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onUpdateClick() },
            enabled = itemUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.reset_password))
        }
    }
}

@Composable
fun ForgotForm(
    modifier: Modifier = Modifier,
    forgotForm: ForgotFormState,
    onValueChange: (ForgotFormState) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = forgotForm.email,
            onValueChange = { onValueChange(forgotForm.copy(email = it)) },
            label = { Text(text = stringResource(R.string.user_email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}