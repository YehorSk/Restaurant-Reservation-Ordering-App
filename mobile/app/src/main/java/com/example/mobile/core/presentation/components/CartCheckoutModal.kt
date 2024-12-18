package com.example.mobile.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.mobile.ui.theme.MobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutModal(
    onDismiss:()->Unit,
    modifier: Modifier = Modifier,
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        content = {
            CartCheckoutModalContent(
                onDismiss,
                modifier = modifier
            )
        }
    )
}

@Composable
fun CartCheckoutModalContent(
    onDismiss:()->Unit,
    modifier: Modifier = Modifier
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text("Checkout Modal")
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCartCheckoutModal() {
    MobileTheme {
        CartCheckoutModalContent(
            onDismiss = {}
        )
    }
}
