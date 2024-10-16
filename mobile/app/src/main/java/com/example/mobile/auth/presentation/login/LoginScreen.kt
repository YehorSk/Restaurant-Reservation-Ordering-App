package com.example.mobile.auth.presentation.login

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onSuccessClient: () -> Unit,
    onSuccessWaiter: () -> Unit,
    onRegClick: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    SingleEventEffect(loginViewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
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
                onRegClick = onRegClick
            )
        }
        val role = loginViewModel.userRole.collectAsStateWithLifecycle().value

        LaunchedEffect(uiState.isLoggedIn) {
            Timber.tag("LaunchedEffect").v("UI State Is Logged In: ${uiState.isLoggedIn} $role")
            if(uiState.isLoggedIn){
                when(role.toString()){
                    "user" -> {
                        Timber.d("Navigating to home screen from Login User")
                        onSuccessClient()
                    }
                    "waiter" -> {
                        Timber.d("Navigating to home screen from Login Waiter")
                        onSuccessWaiter()
                    }
                    else -> {

                    }
                }
            }
        }
        LaunchedEffect(uiState.internetError) {
            if(uiState.internetError){
                Toast.makeText(context,"No internet connection!",Toast.LENGTH_LONG).show()
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        LogForm(
            loginForm = itemUiState.loginForm,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = !itemUiState.isLoading
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onLogClick,
            enabled = itemUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "LogIn")
        }
        Button(
            onClick = onRegClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "SignUp")
        }
        TextButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Forgot Password?")
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = loginForm.email,
            onValueChange = { onValueChange(loginForm.copy(email = it)) },
            label = { Text(text = "User Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = loginForm.password,
            onValueChange = { onValueChange(loginForm.copy(password = it)) },
            label = { Text(text = "User Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
        )
    }
}