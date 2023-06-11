package com.example.mosisprojekat.ui.screens.main.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val gyms = mutableStateOf<List<Gym>>(emptyList())

    fun onMapLongCLick(location: LatLng) = viewModelScope.launch {
        //TODO add repository call when firebase implementation is completed
        gyms.value += Gym(
            name = "HardcodedGym",
            lat = location.latitude,
            lng = location.longitude,
            documentId = "HardcodedID"
        )
    }

    fun onInfoWindowLongCLick(selectedGym: Gym) = viewModelScope.launch {
        navigateToGymDetailsScreen(selectedGym.documentId)
    }

    private fun navigateToGymDetailsScreen(selectedGymId: String) = viewModelScope.launch {
        events.emit(Events.NavigateToGymDetailsScreen(selectedGymId))
    }

    sealed class Events {
        data class NavigateToGymDetailsScreen(val selectedGymId: String): Events()
    }

}