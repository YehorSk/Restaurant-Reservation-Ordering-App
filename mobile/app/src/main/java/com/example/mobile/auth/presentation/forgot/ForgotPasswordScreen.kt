package com.example.mobile.auth.presentation.forgot

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.auth.presentation.login.LogBody
import com.example.mobile.auth.presentation.login.LogForm
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.login.LoginState
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    forgotViewModel: ForgotViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by forgotViewModel.uiState.collectAsStateWithLifecycle()

    SingleEventEffect(forgotViewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(uiState.internetError) {
        if(uiState.internetError){
            Toast.makeText(context,"No internet connection!",Toast.LENGTH_LONG).show()
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
            )
        }
    }
}

@Composable
fun ForgotBody(
    itemUiState: ForgotState,
    modifier: Modifier = Modifier
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
            forgotForm = itemUiState.form
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {},
            enabled = itemUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset Password")
        }
    }
}

@Composable
fun ForgotForm(
    modifier: Modifier = Modifier,
    forgotForm: ForgotFormState,
    onValueChange: (ForgotFormState) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = forgotForm.email,
            onValueChange = { onValueChange(forgotForm.copy(email = it)) },
            label = { Text(text = "User Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}