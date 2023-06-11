package com.example.mosisprojekat.ui.screens.main.gymdetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymDetailsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val gymRepository: GymRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val gym = mutableStateOf<Gym?>(null)

    val isEditButtonEnabled = mutableStateOf(false)

    val isDeleteButtonEnabled = mutableStateOf(false)

    val isEditing = mutableStateOf(false)

    val nameTextState = mutableStateOf(gym.value?.name ?: "")

    //val rating = mutableStateOf(5.0)

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun onEnableEditButtonClick() = viewModelScope.launch {
        isEditing.value = !isEditing.value
        if (!isEditing.value)
            nameTextState.value = gym.value?.name ?: ""
    }

    fun getGymData(gymId: String) = viewModelScope.launch {
        try {
            gymRepository.getGym(
                gymId = gymId,
                onSuccess = {

                    val isCreator = authRepository.getUserId() == gym.value?.userId

                    gym.value = it
                    nameTextState.value = gym.value?.name ?: ""
                    isEditButtonEnabled.value = isCreator
                    isDeleteButtonEnabled.value = isCreator
                },
                onError = {
                    makeGenericErrorToast()
                    it?.printStackTrace()
                }
            )
        } catch(e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    fun onEditGymButtonClicked(gymId: String) = viewModelScope.launch {
        if (isEditing.value)
            editGymName(gymId)
        else
            navigateToHomeScreen()
    }

    private fun editGymName(gymId: String) = viewModelScope.launch {
        try {
            gymRepository.updateGymName(
                gymId = gymId,
                name = nameTextState.value
            ) { isSuccessful ->
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

    fun deleteGym(gymId: String) = viewModelScope.launch {
        try {
            gymRepository.deleteGym(gymId) { isSuccessful ->
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

    fun onSeeReviewsButtonClicked(gymId: String) = viewModelScope.launch {
        events.emit(Events.NavigateToSeeReviewsScreen(gymId))
    }

    fun onAddReviewButtonClicked(gymId: String) = viewModelScope.launch {
        events.emit(Events.NavigateToAddReviewScreen(gymId))
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
        data class NavigateToAddReviewScreen(val selectedGymId: String): Events()
        data class NavigateToSeeReviewsScreen(val selectedGymId: String): Events()
    }
}