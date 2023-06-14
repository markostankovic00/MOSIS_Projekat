package com.example.mosisprojekat.ui.screens.main.userdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsScreenViewModel @Inject constructor(
    private val userDataRepository: UsersDataRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    fun loadUserData(userId: String) = viewModelScope.launch {
        userDataRepository.getUserData(
            userId = userId,
            onError = {
                makeGenericErrorToast()
                it?.printStackTrace()
            },
            onSuccess = { userData ->
                _userData.value = userData
            }
        )
    }

    fun onOkClicked() = viewModelScope.launch {
        events.emit(Events.NavigateToRankingsScreen)
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object NavigateToRankingsScreen: Events()
        object MakeGenericErrorToast: Events()
    }
}