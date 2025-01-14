package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickupMap(){

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
                isMyLocationEnabled = true
            )
        }
        val mapUiSettings = remember {
            MapUiSettings(
                zoomControlsEnabled = false
            )
        }
        GoogleMap(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ) {
            Marker(
                state = charmMarkerState,
                title = "PLATEA Restaurant",
                snippet = "PLATEA"
            )
        }
        Text(
            modifier = Modifier
                .padding(
                    top = 15.dp,
                    bottom = 10.dp
                ),
            text = "PLATEA Restaurant",
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