package com.example.mosisprojekat.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val gymRepository: GymRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _listOfGyms = MutableStateFlow<List<Gym>>(emptyList())
    var listOfGyms = _listOfGyms.asStateFlow()

    init {
        try {
            loadGyms()

        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
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