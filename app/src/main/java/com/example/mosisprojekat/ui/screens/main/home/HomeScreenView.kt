package com.example.mosisprojekat.ui.screens.main.home

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.activities.MainViewModel
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.util.ComponentSizes
import com.example.mosisprojekat.ui.screens.main.home.HomeScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.BackgroundPatternColor
import com.example.mosisprojekat.ui.theme.GreenValid
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.example.mosisprojekat.ui.uiutil.composables.SearchBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()

    val context = LocalContext.current

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    mainViewModel.checkIfGPSEnabled(locationManager)

    EventsHandler(navController, viewModel)
    HomeScreenView(mainViewModel, viewModel)
}

@FlowPreview
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

    val searchText by viewModel.searchText.collectAsState()

    val radiusFilter by viewModel.radiusFilter.collectAsState()

    val isErrorMessagePairRadiusFilter by remember { viewModel.isErrorMessagePairRadiusFilter }

    val showOnlyMineFilter by viewModel.showOnlyMineFilter.collectAsState()

    val gyms by viewModel.listOfGyms.collectAsState()

    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = lastKnownLocation) {
        viewModel.updateLocation(lastKnownLocation)

        lastKnownLocation?.let { location ->
            if (!completedInitialZoom) {
                cameraPositionState.centerOnLocation(location)
                if (cameraPositionState.position.zoom == 15f)
                    completedInitialZoom = true
            }
        }
    }



    Column(
        modifier = Modifier
            .padding(bottom = ComponentSizes.bottomNavBarHeight.dp)
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            BackgroundPatternColor,
                            MaterialTheme.colors.background
                        )
                    ),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            SearchBar(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.large,
                        start = MaterialTheme.spacing.large
                    )
                    .width(280.dp)
                    .height(60.dp),
                searchText = searchText,
                onSearchTextChange = viewModel::onSearchTextChanged,
                placeholderText = stringResource(id = R.string.search_bar_by_name)
            )

            PrimaryOutlinedTextField(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.medium,
                        start = MaterialTheme.spacing.large,
                    ),
                textStateValue = radiusFilter,
                onValueChange = viewModel::onRadiusFilterChanged,
                label = stringResource(id = R.string.home_screen_radius_label),
                onTrailingIconClick = { viewModel.onRadiusFilterChanged("") },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                onDone = { focusManager.clearFocus() }
            )

            Text(
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.large + MaterialTheme.spacing.extraSmall),
                text = isErrorMessagePairRadiusFilter.second,
                color = if (isErrorMessagePairRadiusFilter.first) RedError else GreenValid
            )

            Row(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.extraSmall,
                        start = MaterialTheme.spacing.large,
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = showOnlyMineFilter,
                    onCheckedChange = { viewModel.onShowOnlyMineFilterCheckedChange(it) }
                )
                Text(text = "Show only mine")
            }
        }



        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            onMapLongClick = { viewModel.onMapLongCLick(it) },
            onMapClick = { focusManager.clearFocus() }
        ) {
            gyms.forEach { gym ->
                Marker(
                    position = LatLng(gym.lat, gym.lng),
                    title = gym.name,
                    snippet = stringResource(id = R.string.home_screen_click_on_info_window_text),
                    onInfoWindowClick = { viewModel.onInfoWindowCLick(gym) },
                    onInfoWindowLongClick = { viewModel.onInfoWindowCLick(gym) },
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_VIOLET
                    )
                )
            }
        }
    }
}

@FlowPreview
@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {

            is Events.NavigateToGymDetailsScreen -> {
                val selectedGymId = (event.value as Events.NavigateToGymDetailsScreen).selectedGymId
                navController.navigate(Routes.GYM_DETAILS_SCREEN + "/" + selectedGymId)
            }

            is Events.NavigateToAddGymScreen -> {
                val lat = (event.value as Events.NavigateToAddGymScreen).lat.toString()
                val lng = (event.value as Events.NavigateToAddGymScreen).lng.toString()
                navController.navigate(Routes.ADD_GYM_SCREEN + "/" + lat + "/" + lng)
            }

            Events.MakeGenericErrorToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.error_generic),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

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