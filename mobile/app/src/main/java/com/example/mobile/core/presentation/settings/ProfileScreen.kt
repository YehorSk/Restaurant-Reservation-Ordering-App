package com.example.mobile.core.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.components.NavBar

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onGoBack: () -> Unit
) {
    val name by viewModel.userName.collectAsStateWithLifecycle()
    val address by viewModel.userAddress.collectAsStateWithLifecycle()
    val phone by viewModel.userPhone.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val contentColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoBack()
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
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
            color = contentColor,
            fontWeight = FontWeight.ExtraBold,
        )
        if(name != null){
            ProfileScreenForm(
                name = name ?: "",
                address = address ?: "",
                phone = phone ?: "",
                onUpdateProfile = { name,address,phone -> viewModel.updateProfile(name,address,phone) }
            )
        }
    }
}

@Composable
fun ProfileScreenForm(
    name: String,
    address: String,
    phone: String,
    onUpdateProfile: (String,String,String) -> Unit,
    modifier: Modifier = Modifier
) {

    var nameInput by remember { mutableStateOf(name) }
    var addressInput by remember { mutableStateOf(address) }
    var phoneInput by remember { mutableStateOf(phone) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text(text = stringResource(R.string.user_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = phoneInput,
            onValueChange = { phoneInput = it },
            label = { Text(text = stringResource(R.string.user_phone)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            label = { Text(text = stringResource(R.string.user_address)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { onUpdateProfile(nameInput,addressInput,phoneInput) },
            enabled = nameInput.length != 0 && addressInput.length != 0 && phoneInput.length != 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.update_profile))
        }
    }
}
