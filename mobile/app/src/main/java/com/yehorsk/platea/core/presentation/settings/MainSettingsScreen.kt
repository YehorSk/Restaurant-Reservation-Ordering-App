package com.yehorsk.platea.core.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.presentation.settings.components.ProfileListHeader
import com.yehorsk.platea.core.presentation.settings.components.ProfileListItem
import com.yehorsk.platea.core.utils.toString

@Composable
fun MainSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    onNavigate: (ProfileDestination) -> Unit
){
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> { onNavigate(ProfileDestination.Logout) }
        }
    }

    if(userRole != null){
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item{
                    ProfileListHeader(
                        text = stringResource(R.string.hello_user, userName.toString())
                    )
                }
                item{
                    ProfileListItem(
                        text = R.string.profile,
                        onClick = {
                            onNavigate(ProfileDestination.Profile)
                        }
                    )
                }
                item{
                    ProfileListItem(
                        text = R.string.notifications,
                        onClick = {
                            onNavigate(ProfileDestination.Notifications)
                        }
                    )
                }
                if(userRole in arrayOf("user","waiter","admin")){
                    item{
                        ProfileListItem(
                            text = R.string.reservations,
                            onClick = {
                                onNavigate(ProfileDestination.Reservations)
                            }
                        )
                    }
                }
                if(userRole in arrayOf("user","waiter","admin")){
                    item{
                        ProfileListItem(
                            text = R.string.orders,
                            onClick = {
                                onNavigate(ProfileDestination.Orders)
                            }
                        )
                    }
                }
                item{
                    ProfileListItem(
                        text = R.string.theme,
                        onClick = {
                            onNavigate(ProfileDestination.Theme)
                        }
                    )
                }
                item{
                    ProfileListItem(
                        text = R.string.language,
                        onClick = {
                            onNavigate(ProfileDestination.Language)
                        }
                    )
                }
                item{
                    ProfileListItem(
                        text = R.string.logout,
                        onClick = {
                            viewModel.logout()
                        }
                    )
                }
            }
        }
    }else{
        LoadingPart()
    }
}