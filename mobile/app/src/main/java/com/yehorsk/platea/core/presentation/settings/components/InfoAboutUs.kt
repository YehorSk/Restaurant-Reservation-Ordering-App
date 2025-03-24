package com.yehorsk.platea.core.presentation.settings.components

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileThemePreview

@Composable
fun InfoAboutUs(
    modifier: Modifier = Modifier,
    description: String,
    phone: String,
    email: String,
    website: String,
    address: String
){
    val context = LocalContext.current
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        InfoPart(
            title = R.string.about_us,
            text = description
        )
        InfoPart(
            title = R.string.phone,
            text = phone,
            showButton = true,
            onAction = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:$phone".toUri()
                }
                context.startActivity(intent)
            }
        )
        InfoPart(
            title = R.string.email,
            text = email,
            showButton = true,
            onAction = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:$email".toUri()
                }
                context.startActivity(intent)
            }
        )
        InfoPart(
            title = R.string.website,
            text = website,
            showButton = true,
            onAction = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = website.toUri()
                }
                context.startActivity(intent)
            }
        )
        InfoPart(
            title = R.string.address,
            text = address,
            showButton = true,
            onAction = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "geo:0,0?q=$address".toUri()
                }
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun InfoPart(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    text: String,
    showButton: Boolean = false,
    onAction: () -> Unit = {}
){
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
            start = 20.dp,
            top = 10.dp,
            bottom = 5.dp
        )
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        if(showButton){
            TextButton(
                onClick = {
                    onAction()
                },
                content = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        )
                }
            )
        }else{
            Text(
                modifier = Modifier
                    .padding(top = 5.dp),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
fun InfoPartPreview(){
    MobileThemePreview {
        InfoPart(
            title = R.string.website,
            text = "https://platea.site"
        )
    }
}