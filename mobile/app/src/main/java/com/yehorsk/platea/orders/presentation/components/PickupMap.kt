package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickupMap(
    onMapLoaded: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
    ) {
        val charm = LatLng(48.310755785251786, 18.086364404594054)
        val charmMarkerState = rememberMarkerState(position = charm)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(charm, 10f)
        }
        val mapProperties = remember {
            MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL
            )
        }
        val mapUiSettings = remember {
            MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false
            )
        }
        var mapVisible by remember { mutableStateOf(true) }
        if(mapVisible){
            GoogleMap(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) ,
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMapLoaded = onMapLoaded
            ) {
                Marker(
                    state = charmMarkerState,
                    title = stringResource(R.string.platea_restaurant),
                    snippet = stringResource(R.string.app_name)
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(
                    top = 15.dp,
                    bottom = 10.dp
                ),
            text = stringResource(R.string.platea_restaurant),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            )
        Text(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 10.dp
                ),
            text = "Farska 1315/41, Nitra",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}