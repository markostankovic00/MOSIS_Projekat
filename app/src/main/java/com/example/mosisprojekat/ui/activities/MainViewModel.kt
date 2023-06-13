package com.example.mosisprojekat.ui.activities


import android.location.Location
import android.location.LocationManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val bottomNavBarVisibilityState = (mutableStateOf(false))

    val lastKnownLocation = mutableStateOf<Location?>(null)

    val gpsEnabled = mutableStateOf(false)

    fun checkIfGPSEnabled(locationManager: LocationManager) {
        try {
            gpsEnabled.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Timber.i("Logging: GPS error")
        }
    }

    fun updateLocation(location: Location) = viewModelScope.launch {
        lastKnownLocation.value = location
    }

    fun setBottomNavBarVisibilityState(currentRoute: String?) = viewModelScope.launch {
        bottomNavBarVisibilityState.value = currentRoute in listOf(
            Routes.HOME_SCREEN,
            Routes.PROFILE_SCREEN,
            Routes.RANKINGS_SCREEN,
            Routes.GYM_LIST_SCREEN
        )
    }

    fun onBottomNavItemClicked(itemRoute: String) = viewModelScope.launch {
        when (itemRoute) {
            Routes.HOME_SCREEN -> events.emit(Events.NavigateToHome)
            Routes.PROFILE_SCREEN -> events.emit(Events.NavigateToProfile)
            Routes.RANKINGS_SCREEN -> events.emit(Events.NavigateToRankings)
            Routes.GYM_LIST_SCREEN -> events.emit(Events.NavigateToGymList)
        }
    }

    fun makeLocationErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeLocationErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {

        object NavigateToHome: Events()
        object NavigateToProfile: Events()
        object NavigateToRankings: Events()
        object NavigateToGymList: Events()
        object MakeLocationErrorToast: Events()
    }
}