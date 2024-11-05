package com.example.mobile.menu.presentation.menu_admin.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.mobile.ui.theme.MobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuModal(
    onDismiss:()->Unit,
    modifier: Modifier = Modifier,
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        EditMenuModalContent()
    }

}

@Composable
fun EditMenuModalContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text(text = "Menu Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text(text = "Menu Description") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )
    }
}

@PreviewLightDark
@Composable
fun EditMenuModalContentPreview(){
    MobileTheme {
        EditMenuModalContent()
    }
}