package com.yehorsk.platea.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    @StringRes title: Int = R.string.go_back,
    showGoBack: Boolean = true
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .height(60.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(showGoBack){
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        onGoBack()
                    },
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            text = stringResource(title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            style = LocalTextStyle.current.merge(
                TextStyle(
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    ),
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeight = 3.2.em,
                )
            )
        )
    }
}

@Preview
@Composable
fun NavBarPreview(){
    MobileTheme {
        NavBar(
            onGoBack = {},
            title = R.string.complete_order_navbar_title
        )
    }
}