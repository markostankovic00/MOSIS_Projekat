package com.example.mosisprojekat.ui.screens.main.addgym

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGymScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val userDataRepository: UsersDataRepositoryInteractor,
    private val gymRepository: GymRepositoryInteractor
):ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val nameTextState = mutableStateOf("")

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun addGym(
        lat: Double,
        lng: Double
    ) = viewModelScope.launch {

        try {
            gymRepository.addGym(
                userId = authRepository.getUserId(),
                name = nameTextState.value,
                lat = lat,
                lng = lng
            ) { isSuccessful ->
                if (isSuccessful)
                    addUserPoints()
                else
                    makeGenericErrorToast()
            }
        }  catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun addUserPoints() = viewModelScope.launch {
        try {

            val currentUserId = authRepository.getUserId()

            userDataRepository.updateUserPoints(currentUserId, 200) { isSuccessful ->
                if (isSuccessful)
                    navigateToHomeScreen()
                else
                    makeGenericErrorToast()
            }

        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object NavigateToHomeScreen: Events()
        object MakeGenericErrorToast: Events()
    }
}