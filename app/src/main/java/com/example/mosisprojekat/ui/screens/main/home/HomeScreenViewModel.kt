package com.example.mosisprojekat.ui.screens.main.home

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import com.example.mosisprojekat.util.validateRadius
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val gymRepository: GymRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val searchText = MutableStateFlow("")

    val radiusFilter = MutableStateFlow("")

    val isErrorMessagePairRadiusFilter = mutableStateOf(Pair(false, ""))

    val showOnlyMineFilter = MutableStateFlow(false)

    private var lastKnownLocation: Location? = null

    private val _listOfGyms = MutableStateFlow<List<Gym>>(emptyList())
    val listOfGyms = provideFilteredGymsFlow()

    init {
        try {
            loadGyms()

        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    fun updateLocation(location: Location?) = viewModelScope.launch {
        lastKnownLocation = location
    }

    fun onSearchTextChanged(text: String) {
        searchText.value = text
    }

    fun onRadiusFilterChanged(radius: String) {
        radiusFilter.value = radius
        isErrorMessagePairRadiusFilter.value = validateRadius(radius)
    }

    fun onShowOnlyMineFilterCheckedChange(checked: Boolean) {
        showOnlyMineFilter.value = checked
    }

    fun onMapLongCLick(location: LatLng) = viewModelScope.launch {
        navigateToAddGymScreen(location.latitude, location.longitude)
    }

    fun onInfoWindowCLick(selectedGym: Gym) = viewModelScope.launch {
        navigateToGymDetailsScreen(selectedGym.documentId)
    }

    private fun loadGyms() = viewModelScope.launch {
        gymRepository.getAllGyms().collect {
            _listOfGyms.value = it.data ?: emptyList()
        }
    }

    private fun provideFilteredGymsFlow() =

        searchText
            .debounce(300L)
            .combine(_listOfGyms) { text, gyms ->
                if (text.isBlank()) {
                    gyms
                } else {
                    gyms.filter { gym ->
                        gym.doesMatchSearchQuery(text)
                    }
                }
            }
            .combine(showOnlyMineFilter) { gyms, showOnlyMine ->
                if (!showOnlyMine)
                    gyms
                else
                    gyms.filter {  gym ->
                        gym.userId == authRepository.getUserId()
                    }
            }
            .combine(radiusFilter) { gyms, radius ->
                if (isErrorMessagePairRadiusFilter.value.first || radius.isBlank())
                    gyms
                else
                    gyms.filter {
                        val gymLocation = Location("GymLocation")
                        gymLocation.longitude = it.lng
                        gymLocation.latitude = it.lat
                        val distance = lastKnownLocation?.distanceTo(gymLocation) ?: 0f
                        distance <= radius.toInt()
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                _listOfGyms.value
            )


    private fun navigateToAddGymScreen(lat: Double, lng: Double) = viewModelScope.launch {
        events.emit(Events.NavigateToAddGymScreen(lat, lng))
    }

    private fun navigateToGymDetailsScreen(selectedGymId: String) = viewModelScope.launch {
        events.emit(Events.NavigateToGymDetailsScreen(selectedGymId))
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        data class NavigateToGymDetailsScreen(val selectedGymId: String): Events()
        data class NavigateToAddGymScreen(val lat: Double, val lng: Double): Events()
        object MakeGenericErrorToast: Events()

    }

}