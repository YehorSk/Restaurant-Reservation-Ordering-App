package com.yehorsk.platea.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R

@Composable
fun NavBarWithSearch(
    modifier: Modifier = Modifier,
    text: String,
    onGoBack: () -> Unit,
    onTextChanged: (String) -> Unit,
    showGoBack: Boolean = true
){

    var isSearchBarVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(showGoBack){
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        onGoBack()
                    },
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background),
            value = text,
            onValueChange = { onTextChanged(it) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search)
                )
            }
        )

    }
}

@Preview
@Composable
fun NavBarWithSearchPreview(){
    NavBarWithSearch(
        onGoBack = {},
        text = "",
        onTextChanged = {}
    )
}