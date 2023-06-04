package com.example.mosisprojekat.ui.screens.main.home

import android.location.Location
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.activities.MainViewModel
import com.example.mosisprojekat.util.ComponentSizes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()

    EventsHandler(navController, viewModel)
    HomeScreenView(mainViewModel, viewModel)
}

@Composable
private fun HomeScreenView(
    mainViewModel: MainViewModel,
    viewModel: HomeScreenViewModel
) {

    val lastKnownLocation by remember { mainViewModel.lastKnownLocation }

    val mapProperties = MapProperties(
        isMyLocationEnabled = lastKnownLocation != null,
    )

    val cameraPositionState = rememberCameraPositionState()

    var completedInitialZoom by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = lastKnownLocation) {
        lastKnownLocation?.let { location ->
            if(!completedInitialZoom) {
                cameraPositionState.centerOnLocation(location)
                completedInitialZoom = true
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(bottom = ComponentSizes.bottomNavBarHeight.dp)
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {

        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            else -> {}
        }
    }

}

private suspend fun CameraPositionState.centerOnLocation(
    location: Location
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(location.latitude, location.longitude),
        15f
    )
)