package com.example.mosisprojekat.ui.screens.main.addreview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewScreenViewModel @Inject constructor(
    private val gymRepository: GymRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val commentTextState = mutableStateOf("")

    val markState = mutableStateOf(0)

    fun onCommentTextChanged(comment: String) {
        commentTextState.value = comment
    }

    fun onStarClick(index: Int) {
        markState.value = index
    }

    fun onSaveReviewClick(selectedGymId: String) = viewModelScope.launch {
        try {
            gymRepository.addGymReview(
                gymId = selectedGymId,
                comment = commentTextState.value,
                mark = markState.value
            ) { isSuccessful ->
                if (isSuccessful) {
                    navigateToGymDetailsScreen()
                }
                else {
                    makeGenericErrorToast()
                }
            }
        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun navigateToGymDetailsScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToGymDetailsScreen)
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object NavigateToGymDetailsScreen: Events()
        object MakeGenericErrorToast: Events()
    }
}