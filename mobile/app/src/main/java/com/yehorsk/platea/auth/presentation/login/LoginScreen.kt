package com.yehorsk.platea.auth.presentation.login

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
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.toString
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    onSuccessClient: () -> Unit,
    onSuccessWaiter: () -> Unit,
    onSuccessAdmin: () -> Unit,
    onSuccessChef: () -> Unit,
    onRegClick: () -> Unit,
    onForgotPwdClick: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by loginViewModel.isNetwork.collectAsStateWithLifecycle(false)

    SingleEventEffect(loginViewModel.sideEffectFlow) { sideEffect ->
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
            LogBody(
                itemUiState = uiState,
                onItemValueChange = loginViewModel::updateLogUiState,
                onLogClick = {
                    coroutineScope.launch {
                        loginViewModel.login()
                    }
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                onRegClick = onRegClick,
                onForgotPwdClick = onForgotPwdClick,
                isConnected = isConnected
            )
        }
        val role = loginViewModel.userRole.collectAsStateWithLifecycle().value

        LaunchedEffect(uiState.isLoggedIn) {
            Timber.tag("LaunchedEffect").v("UI State Is Logged In: ${uiState.isLoggedIn} $role")
            if(uiState.isLoggedIn){
                when(role.toString()){
                    "user" -> onSuccessClient()
                    "waiter" -> onSuccessWaiter()
                    "admin" -> onSuccessAdmin()
                    "chef" -> onSuccessChef()
                    else -> {}
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun LogBody(
    itemUiState: LoginState,
    onItemValueChange: (LoginForm) -> Unit,
    onLogClick: () -> Unit,
    onRegClick: () -> Unit,
    onForgotPwdClick: () -> Unit,
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center
        )
        LogForm(
            loginForm = itemUiState.loginForm,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = !itemUiState.isLoading
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onLogClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            enabled = itemUiState.isEntryValid && isConnected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.log_in),
                style = MaterialTheme.typography.bodyLarge
                )
        }
        Button(
            onClick = onRegClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        TextButton(
            onClick = onForgotPwdClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun LogForm(
    modifier: Modifier = Modifier,
    loginForm: LoginForm,
    enabled: Boolean = true,
    onValueChange: (LoginForm) -> Unit = {}
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = loginForm.email,
            onValueChange = { onValueChange(loginForm.copy(email = it)) },
            label = { Text(text = stringResource(R.string.user_email)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = loginForm.password,
            onValueChange = { onValueChange(loginForm.copy(password = it)) },
            label = { Text(text = stringResource(R.string.user_password)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisibility) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                IconButton(onClick = {passwordVisibility = !passwordVisibility}){
                    Icon(imageVector  = image, description)
                }
            },
        )
    }
}