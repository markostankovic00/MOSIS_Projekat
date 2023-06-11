package com.example.mosisprojekat.ui.screens.main.seereviews

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.Review
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SeeReviewsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val gymRepository: GymRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val reviews = MutableStateFlow<List<Review>>(emptyList())

    val currentUserId = mutableStateOf("")

    fun loadReviews(selectedGymId: String) = viewModelScope.launch {
        try {

            gymRepository.getGym(
                selectedGymId,
                onError = {
                    makeGenericErrorToast()
                    it?.printStackTrace()
                },
                onSuccess = { gym ->
                    reviews.value = gym?.reviews ?: emptyList()
                }
            )

        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    fun loadCurrentUser() = viewModelScope.launch {
        try {
            currentUserId.value = authRepository.getUserId()
        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    fun onDeleteReviewClicked(review: Review, selectedGymId: String) = viewModelScope.launch {
        try {
            gymRepository.removeGymReview(
                gymId = selectedGymId,
                review = review
            ) { isSuccessful ->
                if (isSuccessful) {
                    loadReviews(selectedGymId)
                    makeSuccessfulDeletionToast()
                } else {
                    makeGenericErrorToast()
                }
            }
        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun makeSuccessfulDeletionToast() = viewModelScope.launch {
        events.emit(Events.MakeSuccessfulDeletionToast)
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object MakeGenericErrorToast: Events()
        object MakeSuccessfulDeletionToast: Events()
    }

}