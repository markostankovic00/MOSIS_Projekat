package com.example.mosisprojekat.ui.screens.main.addgym

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGymScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val gymRepository: GymRepositoryInteractor
):ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val nameTextState = mutableStateOf("")

    val commentTextState = mutableStateOf("")

    val rating = mutableStateOf(5.0)

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun onCommentTextChanged(comment: String) {
        commentTextState.value = comment
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
                lng = lng,
                comment = commentTextState.value,
                rating = rating.value
            ) { isSuccessful ->
                if (isSuccessful)
                    navigateToHomeScreen()
                else
                    makeGenericErrorToast()
            }
        }  catch (e: Exception) {
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