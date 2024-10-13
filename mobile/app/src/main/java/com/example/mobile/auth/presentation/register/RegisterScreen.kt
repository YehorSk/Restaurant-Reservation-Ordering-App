package com.example.mobile.auth.presentation.register

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    authViewModel: RegisterViewModel = hiltViewModel(),
    onLogClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

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
            RegBody(
                itemUiState = uiState,
                onItemValueChange = authViewModel::updateRegUiState,
                onRegClick = {
                    coroutineScope.launch {
                        authViewModel.register()
                    }
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                onLogClick = onLogClick,
                itemErrorUiState = uiState.registerFormErrors
            )
        }
        LaunchedEffect(uiState.isLoggedIn) {
            Timber.tag("LaunchedEffect").v("UI State Is Logged In: ${uiState.isLoggedIn}")
            if(uiState.isLoggedIn){
                Timber.d("Navigating to home screen from Register")
                onSuccess()
            }
        }
        LaunchedEffect(uiState.internetError) {
            if(uiState.internetError){
                Toast.makeText(context,"No internet connection!", Toast.LENGTH_LONG).show()
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
fun RegBody(
    itemUiState: RegisterState,
    itemErrorUiState: RegisterFormErrors,
    onItemValueChange: (RegisterForm) -> Unit,
    onRegClick: () -> Unit,
    onLogClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        RegForm(
            registerForm = itemUiState.registerForm,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            itemErrorUiState = itemErrorUiState,
            enabled = !itemUiState.isLoading
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onRegClick,
            enabled = itemUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
        Button(
            onClick = onLogClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "LogIn")
        }
    }
}


@Composable
fun RegForm(
    registerForm: RegisterForm,
    itemErrorUiState: RegisterFormErrors,
    enabled: Boolean = true,
    onValueChange: (RegisterForm) -> Unit = {},
    modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val errorName = (itemErrorUiState.name != "[]" && itemErrorUiState.name != "")
        val errorEmail = (itemErrorUiState.email != "[]" && itemErrorUiState.email != "")
        val errorPwd = (itemErrorUiState.password != "[]" && itemErrorUiState.password != "")
        val errorPwdCnf = (itemErrorUiState.passwordConfirm != "[]" && itemErrorUiState.passwordConfirm != "")
        OutlinedTextField(
            value = registerForm.name,
            onValueChange = { onValueChange(registerForm.copy(name = it)) },
            label = { Text(text = "User Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorName,
            supportingText = {
                if (errorName) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.name,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorName)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
        OutlinedTextField(
            value = registerForm.email,
            onValueChange = { onValueChange(registerForm.copy(email = it)) },
            label = { Text(text = "User Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorEmail,
            supportingText = {
                if (errorEmail) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.email,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorEmail)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
        OutlinedTextField(
            value = registerForm.password,
            onValueChange = { onValueChange(registerForm.copy(password = it)) },
            label = { Text(text = "User Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorPwd,
            supportingText = {
                if (errorPwd) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.password,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorPwd)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
        OutlinedTextField(
            value = registerForm.passwordConfirm,
            onValueChange = { onValueChange(registerForm.copy(passwordConfirm = it)) },
            label = { Text(text = "Password Confirm") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorPwdCnf,
            supportingText = {
                if (errorPwdCnf) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.passwordConfirm,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorPwdCnf)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
    }
}

