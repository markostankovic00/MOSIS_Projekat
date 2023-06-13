package com.example.mosisprojekat.ui.screens.main.gymlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymListScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val gymRepository: GymRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val currentUserID = mutableStateOf("")

    private val _listOfGyms = MutableStateFlow<List<Gym>>(emptyList())
    var listOfGyms = _listOfGyms.asStateFlow()

    init {
        try {

            currentUserID.value = authRepository.getUserId()
            loadGyms()

        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    fun onGymCLicked(gymId: String) = viewModelScope.launch {
        navigateToGymDetailsScreen(gymId)
    }

    private fun loadGyms() = viewModelScope.launch {
        gymRepository.getAllGyms().collect {
            _listOfGyms.value = it.data ?: emptyList()
        }
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
        object MakeGenericErrorToast: Events()
    }
}