package com.yehorsk.platea.auth.presentation.register

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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.toString
import com.yehorsk.platea.ui.theme.MobileTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onLogClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
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
            RegBody(
                itemUiState = uiState,
                onItemValueChange = viewModel::updateRegUiState,
                onRegClick = {
                    coroutineScope.launch {
                        viewModel.register()
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
            .padding(8.dp)
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
            Text(text = stringResource(R.string.sign_up))
        }
        Button(
            onClick = onLogClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.log_in))
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
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center
        )
        val errorName = (itemErrorUiState.name != "[]" && itemErrorUiState.name != "")
        val errorEmail = (itemErrorUiState.email != "[]" && itemErrorUiState.email != "")
        val errorPwd = (itemErrorUiState.password != "[]" && itemErrorUiState.password != "")
        val errorPwdCnf = (itemErrorUiState.passwordConfirm != "[]" && itemErrorUiState.passwordConfirm != "")
        OutlinedTextField(
            value = registerForm.name,
            onValueChange = { onValueChange(registerForm.copy(name = it)) },
            label = { Text(text = stringResource(R.string.user_name)) },
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
            label = { Text(text = stringResource(R.string.user_email)) },
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
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        var passwordConfirmVisibility: Boolean by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = registerForm.password,
            onValueChange = { onValueChange(registerForm.copy(password = it)) },
            label = { Text(text = stringResource(R.string.user_password)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisibility) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                IconButton(onClick = {passwordVisibility = !passwordVisibility}){
                    Icon(imageVector  = image, description)
                }
            },
        )
        OutlinedTextField(
            value = registerForm.passwordConfirm,
            onValueChange = { onValueChange(registerForm.copy(passwordConfirm = it)) },
            label = { Text(text = stringResource(R.string.password_confirm)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            visualTransformation = if (passwordConfirmVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                val image = if (passwordConfirmVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordConfirmVisibility) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                IconButton(onClick = {passwordConfirmVisibility = !passwordConfirmVisibility}){
                    Icon(imageVector  = image, description)
                }
            },
        )
    }
}

@Preview
@Composable
fun RegBodyPreview(){
    val fakeRegisterState = RegisterState(
        registerForm = RegisterForm(
            name = "Jane Smith",
            email = "janesmith@example.com",
            password = "securePass456",
            passwordConfirm = "securePass456",
            message = "Ready to register!"
        ),
        registerFormErrors = RegisterFormErrors(
            name = "",
            email = "",
            password = "",
            passwordConfirm = "",
            message = ""
        ),
        isEntryValid = true,
        isLoading = false,
        isLoggedIn = false
    )
    MobileTheme {
        RegBody(
            itemUiState = fakeRegisterState,
            itemErrorUiState = fakeRegisterState.registerFormErrors,
            onRegClick = {},
            onLogClick = {},
            onItemValueChange = {}
        )
    }
}
